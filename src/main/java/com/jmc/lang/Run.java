package com.jmc.lang;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 执行系统内部命令
 * @since 1.0
 * @author Jmc
 */
public class Run {

	private Run() {}

	/**
	 * 同步执行控制台命令
	 * @param command 命令字符串
	 * @return 执行结果
	 * @apiNote <pre>{@code
	 * // 执行命令并将结果放进字符串
	 * var res = Run.execToStr("cmd /c echo 777");
	 * }</pre>
	 */
	public static String execToStr(String command) {
		var proc = Tries.tryReturnsT(() -> Runtime.getRuntime().exec(command));

		var res = new StringBuilder();

		// 输出信息
		try (var msgIn = new InputStreamReader(proc.getInputStream())) {
			int i;
			while ((i = msgIn.read()) != -1) {
				res.append((char) i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 等待程序执行结束
		Tries.tryThis(proc::waitFor);

		// 输出错误信息
		try (var errMsgIn = new InputStreamReader(proc.getErrorStream())) {
			int i;
			while ((i = errMsgIn.read()) != -1) {
				res.append((char) i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res.toString();
	}

	/**
	 * 同步执行控制台命令并打印结果到控制台
	 * @param command 命令字符串
	 * @apiNote <pre>{@code
     * // 执行命令并打印结果
	 * Run.exec("cmd /c echo 666");
	 * }</pre>
	 */
	public static void exec(String command) {
		var proc = Tries.tryReturnsT(() -> Runtime.getRuntime().exec(command));

		// 输出信息
		try (var msgIn = new InputStreamReader(proc.getInputStream())) {
			int i;
			while ((i = msgIn.read()) != -1) {
				System.out.print((char) i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 等待程序执行结束
		Tries.tryThis(proc::waitFor);

		// 输出错误信息
		try (var errMsgIn = new InputStreamReader(proc.getErrorStream())) {
			int i;
			while ((i = errMsgIn.read()) != -1) {
				System.err.print((char) i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	/**
	 * 直接在控制台运行命令
	 * @param command 命令字符串，不能存在双引号
	 * @apiNote <pre>{@code
	 * // 执行命令并打印结果
	 * Run.execOnConsole("cmd /c echo 666");
	 * }</pre>
	 * @since 3.3
	 */
	public static void execOnConsole(String command) {
		// 创建进程构造器，将输出流和错误流重定向到控制台
		var processBuilder = new ProcessBuilder(command.split(" "))
				.redirectOutput(ProcessBuilder.Redirect.INHERIT)
				.redirectError(ProcessBuilder.Redirect.INHERIT);

		// 启动进程并等待执行结束
		var process = Tries.tryReturnsT(processBuilder::start);
		Tries.tryThis(process::waitFor);
	}
}
