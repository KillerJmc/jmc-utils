package com.jmc.process;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 执行系统内部命令
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Run
{
	/**
	 * 同步执行控制台命令
	 * @param command 命令字符串
	 * @return 执行结果
	 */
	public static String execToStr(String command) {
		// 新建异常返回字串符
		String exceptions = "";

		try {				
			// 新建Process
			Process proc = Runtime.getRuntime().exec(command);
		
			// 新建正常信息输入流
			BufferedReader normalMsg = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			// 临时字符串
			String temp;
			String exit = "";
			StringBuilder preserve = new StringBuilder();

			// 若信息流里还有字符串
			while ((temp = normalMsg.readLine()) != null) {
				preserve.append(temp).append("\n");
			}

			// 关闭流
			normalMsg.close();

			// 判断是否执行完
			if (proc.waitFor() != 0) {
				// 新建错误信息输入流
				BufferedReader errorMsg = new BufferedReader(
					new InputStreamReader(proc.getErrorStream())
				);

				// 若信息流里还有字符串
				while ((temp = errorMsg.readLine()) != null) {
					preserve.append(temp).append("\n");
				}

				// 关闭流
				errorMsg.close();

				exit = "\n结束值为: " + proc.exitValue() + "\n";
			}			

			// 返回执行过程系统输出内容
			return preserve + exit;
		} catch (IOException e) {
			// 异常给字符串
			exceptions += "\nIO异常: " + "\n" + e.getCause();
		} catch (InterruptedException e) {
			// 异常给字符串
			exceptions += "\n终止异常: " + "\n" + e.getCause();
		}
		
		// 返回异常
		return exceptions;
	}

	/**
	 * 同步执行控制台命令并打印结果到控制台
	 * @param command 命令字符串
	 */
	public static void exec(String command) {
        System.out.println(execToStr(command));
    }
}
