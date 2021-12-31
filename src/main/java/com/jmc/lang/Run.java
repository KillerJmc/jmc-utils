package com.jmc.lang;
 
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 执行系统内部命令
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Run {

	private Run() {}

	/**
	 * 同步执行控制台命令
	 * @param command 命令字符串
	 * @return 执行结果
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
}
