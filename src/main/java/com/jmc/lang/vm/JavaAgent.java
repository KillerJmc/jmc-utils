package com.jmc.lang.vm;

import com.jmc.io.Files;
import com.jmc.lang.Run;
import com.jmc.lang.Tries;
import com.jmc.lang.reflect.Reflects;
import com.sun.tools.attach.VirtualMachine;

/**
 * Java Agent操作类
 * @since 3.0
 * @author Jmc
 */
public class JavaAgent {
    /**
     * 加载Java Agent到指定的JVM
     * @param pid 虚拟机进程id
     * @param agentJarPath Java Agent的jar路径
     */
    public static void load(String pid, String agentJarPath) {
        if (!Files.exists(agentJarPath)) {
            throw new RuntimeException("Agent的Jar路径不存在：\"" + agentJarPath + '"');
        }

        Tries.tryThis(() -> {
            var vm = VirtualMachine.attach(pid);
            vm.loadAgent(agentJarPath);
            vm.detach();
        });
    }

    /**
     * 用于外部调用，加载Java Agent到指定的JVM
     * @param args pid, agentJarPath
     */
    public static void main(String[] args) {
        var pid = args[0];
        var agentJarPath = args[1];

        load(pid, agentJarPath);
    }

    /**
     * 加载Java Agent到当前JVM
     * @param agentJarPath Java Agent的jar路径
     */
    public static void loadToSelf(String agentJarPath) {
        // 是否为Windows平台
        var isWindowsPlatform = System.getProperty("os.name").startsWith("Windows");

        // java二进制文件路径
        var javaBinPath = System.getProperty("java.home") + "/bin/java";

        // 自身的类路径或者jar路径
        String selfClassPath;

        if (Reflects.isClassInJar(JavaAgent.class)) {
            // 在模块外调用，获取到的是jar路径
            selfClassPath = Reflects.getJarPath(JavaAgent.class);
        } else {
            var classPath = Reflects.getClassPath(JavaAgent.class).orElseThrow();

            // 在模块内调用（junit测试），获取到的是类路径
            selfClassPath = classPath.getPath();
        }

        // 自身class的名称
        var thisClassName = JavaAgent.class.getName();

        // 自身进程的id
        var selfPid = String.valueOf(ProcessHandle.current().pid());

        // -cp的类路径分隔符
        var classPathSeparator = isWindowsPlatform ? ";" : ":";

        // 由于JDK9之后默认禁止将Java Agent加载到自身JVM，所以需要新建另一个Java进程来完成这个工作
        // 通过新开一个java进程执行自身class的main方法，调用load(pid, agentJarPath)来将agent注册进自身JVM
        // "/path/to/java" -cp "jmc-utils.jar$classPathSeparator$agentJarPath" "com.jmc.lang.vm.JavaAgent" "$selfPid" "$agentJarPath"
        var cmd = """
        "%s" -cp "%s%s%s" "%s" "%s" "%s"
        """.formatted(javaBinPath, selfClassPath, classPathSeparator, agentJarPath, thisClassName, selfPid, agentJarPath);

        // 执行命令并获取执行结果
        var res = Run.execToStr(cmd);

        // 如果执行结果不为空，就说明加载agent失败
        if (!res.isBlank()) {
            throw new RuntimeException("Load agent jar failed! Caused by: " + res);
        }
    }
}
