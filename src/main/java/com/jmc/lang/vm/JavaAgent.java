package com.jmc.lang.vm;

import com.jmc.io.Files;
import com.jmc.lang.Run;
import com.jmc.lang.Tries;
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
     * @apiNote <pre>{@code
     * // 加载agent.jar到pid为23333的JVM中
     * JavaAgent.load("23333", "/path/to/agent.jar");
     * }</pre>
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
     * @apiNote <pre>{@code
     * // 加载agent.jar到当前的JVM中
     * JavaAgent.loadToSelf("/path/to/agent.jar");
     * }</pre>
     */
    public static void loadToSelf(String agentJarPath) {
        // java二进制文件路径
        var javaBinPath = System.getProperty("java.home") + "/bin/java";

        // 类加载路径
        var classPaths = System.getProperty("java.class.path");

        // 自身class的名称
        var thisClassName = JavaAgent.class.getName();

        // 自身进程的id
        var selfPid = String.valueOf(ProcessHandle.current().pid());

        // 由于JDK9之后默认禁止将Java Agent加载到自身JVM，所以需要新建另一个Java进程来完成这个工作
        // 通过新开一个java进程执行自身class的main方法，调用load(pid, agentJarPath)来将agent注册进自身JVM
        // "/path/to/java" -cp "$classPaths" "com.jmc.lang.vm.JavaAgent" "$selfPid" "$agentJarPath"
        var cmd = """
        "%s" -cp "%s" "%s" "%s" "%s"
        """.formatted(javaBinPath, classPaths, thisClassName, selfPid, agentJarPath);

        // 执行命令并获取执行结果
        var res = Run.execToStr(cmd);

        // 如果执行结果不为空，就说明加载agent失败
        if (!res.isBlank()) {
            throw new RuntimeException("Load agent jar failed! Caused by: " + res);
        }
    }
}
