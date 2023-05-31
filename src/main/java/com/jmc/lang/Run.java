package com.jmc.lang;

import com.jmc.os.SystemInfo;

import java.io.BufferedReader;
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
	 * var res = Run.execToStr("cmd /c echo 你好");
	 * }</pre>
	 */
	public static String execToStr(String command) {
		// 存放执行结果
		var res = new StringBuilder();

		// 构建进程
		var proc = Tries.tryReturnsT(() -> Runtime.getRuntime().exec(command));

		// 判断系统是否为Windows
		var isWindows = SystemInfo.TYPE == SystemInfo.Type.WINDOWS;

		// 设置输出信息编码
		var encoding = isWindows ? "GBK" : "UTF-8";

		// 获取正常信息
		try (var msgIn = proc.getInputStream()) {
			res.append(new String(msgIn.readAllBytes(), encoding));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 获取错误信息
		try (var errMsgIn = proc.getErrorStream()) {
			res.append(new String(errMsgIn.readAllBytes(), encoding));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 等待程序执行结束
		Tries.tryThis(proc::waitFor);

		return res.toString();
	}

	/**
	 * 同步执行控制台命令并打印结果到控制台
	 * @param command 命令字符串
	 * @apiNote <pre>{@code
     * // 执行命令并打印结果
	 * Run.exec("cmd /c echo 你好");
	 * }</pre>
	 */
	public static void exec(String command) {
		// 构建进程
		var proc = Tries.tryReturnsT(() -> Runtime.getRuntime().exec(command));

		// 判断系统是否为Windows
		var isWindows = SystemInfo.TYPE == SystemInfo.Type.WINDOWS;

		// 设置输出信息编码
		var encoding = isWindows ? "GBK" : "UTF-8";

		// 输出正常信息
		try (var msgIn = new BufferedReader(new InputStreamReader(proc.getInputStream(), encoding))) {
			String line;
			while ((line = msgIn.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 输出错误信息
		try (var errMsgIn = new BufferedReader(new InputStreamReader(proc.getErrorStream(), encoding))) {
			String line;
			while ((line = errMsgIn.readLine()) != null) {
				System.err.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 等待程序执行结束
		Tries.tryThis(proc::waitFor);
    }
}
