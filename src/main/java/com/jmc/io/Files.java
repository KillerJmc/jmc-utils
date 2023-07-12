package com.jmc.io;

import com.jmc.aop.DefaultArg;
import com.jmc.aop.DefaultArgTransfer;
import com.jmc.lang.Objs;
import com.jmc.lang.Strs;
import com.jmc.lang.Tries;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作
 * <p>时间: 2019.1.12
 * <p>更新:
 * <pre>
 *   2019.1.25     1. 增加“源文件是否为文件夹的判断”
 *                 2. 优化提示信息
 *   2019.1.27     1. 优化删除文件(删除一个有2100长文件夹只需38秒)
 *                 2. 添加多线程
 *   2019.1.28     添加随机存储
 *   2019.1.29     添加大文件通道(删除原有随机存储)
 *   2019.1.31     根据设备彻底优化，从最大程度避免出错
 *   2019.2.2      1. 加入zip压缩和解压
 *                 2. 加入文件的移动和重命名
 *   2019.2.3      1. 加入基础提示布尔值
 *                 2. 加入状态信息
 *   2019.2.17     加入输出和读取功能
 *   2019.2.27     读取改用FIS(BR末尾换行，故不采用)
 *   2019.2.28     输出功能加入追加模式
 *   2019.3.15     加入是否检查的布尔值
 *   2019.3.19     设置不抛出所有异常
 *   2019.3.23     复制功能加入追加模式
 *   2019.4.15     做到精准计算操作用时
 *   2019.5.3      通道均改为静态内部类方式
 *   2019.6.23     优化提示信息
 *   2019.7.3      加入输出字节码功能
 *   2019.8.22     1. 改输入字节码功能为readToBytes
 *                 2. 将readToBytes,outBytes作为输入输出主方法
 *   2019.8.23     1. 优化文件压缩和解压，及时关闭文件流和清除缓冲区
 *                 2. 删除out方法中检查文件完整性判断
 *   2019.8.24     1. 添加文件搜索功能
 *                 2. 优化文件信息统计
 *                 3. 删除结果保存功能
 *                 4. 删除多余成员变量
 *                 5. 删除是否复制到sd卡判断
 *                 6. 优化线程结束判断
 *   2019.8.25     1. 高级搜索功能添加过滤器
 *                 2. 调整流关闭顺序(先打开的后关闭，只需关闭Channel无需关闭文件流)
 *   2019.8.31     1. 加入线程池
 *                 2. 修改临时数组大小: 1024(1k) -> 8192(8k) 性能提升明显
 *   2019.9.13     1. 加入文件编码修改功能
 *                 2. 输入输出方法加入字符集参数
 *   2019.10.5     加入单文件搜索(findAny)方法
 *   2020.3.4      用非递归方式统计文件夹和搜索单文件，其中搜索单文件的速度明显加快
 *   2020.3.10     添加批量重命名方法
 *   2020.3.11     1. 修复文件信息方法中的统计错误(将初始文件夹的大小：4KB也统计进去了)
 *                 2. 添加文件长度转换方法
 *   2020.3.12     加入树状图统计文件夹功能
 *   2020.3.31     1. 加入批量移动和批量删除功能
 *                 2. 将批量方法集中并改名为寻找XX
 *                 3. 添加寻找复制方法
 *                 4. 添加findDO模板以统一化代码，并扩展findXX为orContains形式
 *   2020.4.1      1. 升级findToMap方法，可以输入多个搜索条件
 *                 2. 添加deletes方法
 *   2020.4.8      1. 添加outStream方法
 *                 2. 将所有out系列方法(outBytes, outStream)统一命名为out
 *                 3. 压缩方法添加“储存模式”可选项
 *                 4. 优化输出，使统计结果更加美观
 *   2020.4.9      将Streams类的流与文件方法移动到本类
 *   2020.4.17     添加按时间重命名方法
 *   2020.4.20     完善移动方法，使其能兼容不同分区的移动
 *   2020.8.7      1. 添加findFiles和findDirs方法
 *                 2. 将findToMap方法改名为findAll
 *                 3. 删除findAll第一个参数为File的重载方法
 *   2020.8.18     1. 将所有返回布尔值的方法改为返回void且出错时直接抛出运行时异常
 *                 2. 添加deletes参数为文件列表的重载方法
 *   2020.12.27    对源码进行大幅度调整优化，并添加相应方法注释
 *   2021.3.15     更新大文件复制通道代码，用NIO: ByteBuffer提高性能
 *   2021.7.8      使用logger代替basicDetails，showOperatingDetails
 *   2021.7.9      1. 删除部分out方法
 *                 2. 减少成员变量
 *                 3. findAll搜索结果改为类储存，提高辨识度
 *                 4. read方法用Charset类代替charsetName字符串参数，提高可读性
 *                 5. out方法默认采用非追加方式，方便调用
 *   2021.7.10     1. 更改方法次序，使其更合理
 *                 2. 添加mkdirs方法，规范创建多级目录
 *                 3. 用运行时异常代替所有的sout
 *   2021.7.22     优化文件树，使其返回实体对象并且自动按文件/文件夹自动降序排列
 *   2021.8.17     优化Logger打印，在其关闭情况下减少资源损耗（降低了20%耗时）
 *   2021.8.18     懒惰初始化Logger，减少能源损耗（降低3%耗时）
 *   2021.8.21     1. 内联ZipOutputThread和多个loop系列方法（copyLoop等），降低耦合度
 *                 2. 删除两个CopyThread，改为普通方法
 *                 3. 改进拷贝时目标文件路径推算算法，增强源码可读性
 *   2021.8.23     1. 修改Logger名称为类全限定名
 *                 2. 删除enableLog成员变量（boolean）
 *   2021.8.28     添加代码折叠，使总体结构更清晰
 *   2021.11.24    添加exists, createFile和delete(String...)方法
 *   2021.11.25    添加lines方法
 *   2022.3.5      当文件树展开文件夹失败时，改为打印错误日志而不是直接抛出异常
 *   2022.12.29    1. 添加getEncoding系列方法来查询文件编码
 *                 2. 添加setEncoding(file, newEncoding)方法来简化设置文件编码的调用
 *   2022.2.12     1. 添加list方法来列出一级目录的文件/文件夹
 *                 2. 添加默认参数，更改重载方法次序！
 *   2022.2.18     使用默认参数特性（@DefaultArg）重写具有默认参数的方法！
 *   2023.2.22     1. 将所有方法添加@apiNote以标注api示例代码
 *                 2. 删除findDO系列方法（findMoves，findDels等）
 *                 3. 添加必要的注释提高代码可读性
 *   2023.3.6      添加getAbsolutePath方法来从相对路径获取绝对路径
 *   2023.3.23     添加createTempFile和createTempDir方法来新建临时文件和文件夹
 *   2023.4.3      添加getParentPath方法来获取路径的父路径
 *   2023.7.12     1. 添加isFile和isDir方法
 *                 2. 删除findInfo方法
 *                 3. 添加getLength和getFileInfo方法
 * </pre>
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Files
{
	// region init

	/**
	 * 大文件大小界限，用于复制文件时大小文件的区分，以便调用不同的优化复制方法
	 */
	private static final int LARGE_FILE_SIZE = 1024 * 1024 * 500;

	/**
	 * 最多正在操作文件数（用于指定复制等操作线程池最大线程数）
	 */
	private static final int MAX_OPERATING_AMOUNT = 500;

	/**
	 * 全局日志打印
	 */
	private static Logger log;

	private Files() {}

	/**
	 * 设置是否打印日志
	 * @param enable 是否打印日志
	 * @apiNote <pre>{@code
	 * // 开启操作日志打印（复制，删除文件等情况下打印日志）
	 * Files.enableLog(true);
	 * }</pre>
	 */
	public static synchronized void enableLog(boolean enable) {
		if (log == null) {
			if (enable) {
				log = Logger.getLogger(Files.class.getName());
			}
		} else {
			log.setLevel(enable ? Level.INFO : Level.OFF);
		}
	}

	/**
	 * 打印日志（懒加载提高性能）
	 * @param msg 日志内容
	 */
	private static void log(Supplier<String> msg) {
		if (log != null) {
			log.info(msg);
		}
	}

	// endregion

	// region basic

	/**
	 * 复制文件或文件夹
	 * @param srcPath 源路径
	 * @param desPath 目标路径
	 * @apiNote <pre>{@code
	 * // 将a.txt复制到dir文件夹
	 * Files.copy("./a.txt", "/path/to/dir");
	 *
	 * // 将a文件夹复制到/path/to/dir/a
	 * Files.copy("/path/to/a", "/path/to/dir");
	 * }</pre>
	 */
	public static void copy(String srcPath, String desPath) {
        Objs.throwsIfNullOrEmpty("源路径和目标路径不能为空！", srcPath, desPath);

        // 创建源文件
        File src = new File(srcPath);

        // 检查路径是否存在
        if (!src.exists()) {
			throw new RuntimeException("源文件不存在，复制失败");
		}

        // 创建目标文件
        File des = new File(desPath + "/" + src.getName());

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 如果是文件
        if (src.isFile()) {
			log(() -> "正在复制" + src.getName() + "这个文件");

			// 创建父目录
			mkdirs(des.getParent());

			// 如果是小文件
            if (src.length() < LARGE_FILE_SIZE) {
				copySmallFile(src, des);
			} else {
				copyLargeFile(src, des);
			}
        } else {
			log(() -> "正在复制" + src.getName() + "这个文件夹");

			// 线程池
			var pool = Executors.newFixedThreadPool(MAX_OPERATING_AMOUNT);

			// 复制文件夹
			new Object() {
				void loop(File srcDir, File desDir) {
					File[] fs = srcDir.listFiles();
					if (fs == null) {
						throw new RuntimeException("展开文件夹失败");
					}

					for (File src : fs) {
						File des = new File(desDir, src.getName());

						if (src.isDirectory()) {
							// 创建这个目录
							mkdirs(des);

							// 递归复制
							loop(src, des);
						} else {
							pool.execute(src.length() < LARGE_FILE_SIZE ?
									() -> copySmallFile(src, des) : () -> copyLargeFile(src, des));
						}
					}
				}
			}.loop(src, des);

			pool.shutdown();

			// 等待执行完成
			Tries.tryThis(() -> { while (!pool.awaitTermination(1, TimeUnit.DAYS)) {} });
        }

		// 统计时间
		long endTime = System.currentTimeMillis();
		log(() -> "耗时" + (double)((endTime - startTime) / 1000) + "秒，已完成");
    }

	/**
	 * 复制小文件
	 * @param src 源文件
	 * @param des 目标文件
	 */
	private static void copySmallFile(File src, File des) {
		// 日志信息
		log(() -> "正在复制文件: " + src.getAbsolutePath());

		try (var in = new FileInputStream(src);
			 var out = new FileOutputStream(des)) {
			in.transferTo(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制大文件 <br>
	 * 使用的是Java的零拷贝技术
	 * @param src 源文件
	 * @param des 目标文件
	 */
	private static void copyLargeFile(File src, File des) {
		// 日志信息
		log(() -> "正在复制大文件: " + src.getAbsolutePath());

        /*
		1. 采用磁盘映射 -> 内存的Buffer, 直接用指针方式控制该Buffer（Linux: mmap），从而取消Buffer和JVM的双向复制过程

		2. 此方法方便于in.transferTo，因为受硬件限制，transferTo在Windows和Linux下均限制每次最多只能复制2GB
		（FileChannelImpl.transferTo的icount变量限制）

		3. 此方法快于in.transferTo，因为transferTo在Windows下不支持linux的sendfile技术
		（FileChannelImpl.transferToDirectly()）

		4. transferTo最终调用的具体实现与以下循环实现一致，并且其每次循环都新建并释放一个buff（8MB），效率不高
		（FileChannelImpl.transferToTrustedChannel()）
		 */
		try (var in = new FileInputStream(src);
			 var out = new FileOutputStream(des)) {

			// 获取文件通道
			var inChannel = in.getChannel();
			var outChannel = out.getChannel();

			// 申请8M的堆外内存
			var buff = ByteBuffer.allocateDirect(8 * 1024 * 1024);

			// 读取通道中数据到buff
			while (inChannel.read(buff) != -1) {
				// 改变buff为读模式
				buff.flip();
				// 将buff写入输出通道
				outChannel.write(buff);
				// buff的复位
				buff.clear();
			}
			// 强制将内存中剩余数据写入硬盘，保证数据完整性
			outChannel.force(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制文件或文件夹
	 * @param src 源文件
	 * @param destPath 目标路径
	 * @see #copy(String, String)
	 */
	public static void copy(File src, String destPath) {
		copy(src.getAbsolutePath(), destPath);
	}

	/**
	 * 移动文件或文件夹
	 * @param srcPath 源路径
	 * @param desPath 目标路径
	 * @apiNote <pre>{@code
	 * // 将a.txt移动到test文件夹
	 * Files.move("./a.txt", "path/to/test");
	 *
	 * // 将a文件夹移动到/path/to/test/a
	 * Files.move("/path/to/a", "/path/to/test");
	 * }</pre>
	 */
	public static void move(String srcPath, String desPath) {
		Objs.throwsIfNullOrEmpty("根路径和目标路径不能为空", srcPath, desPath);

        // 创建源文件
        File src = new File(srcPath);

        // 判断源文件是否存在
        if (!src.exists()) {
            throw new RuntimeException("源文件不存在");
        }

        // 目标文件
        File des = new File(desPath + "/" + src.getName());

        // 创建目标父目录
		mkdirs(desPath);

		// 移动文件，若移动失败就该用传统方式
		if (!src.renameTo(des)) {
			copy(srcPath, des.getParent());

			if (des.exists()) {
				delete(srcPath);
			} else {
				throw new RuntimeException("\n移动失败！");
			}
		}

        // 日志信息
        log(() -> "成功将 " + src.getName() + " 移动到 " + des.getParentFile().getName() + " 文件夹!");
    }

	/**
	 * 移动文件或文件夹
	 * @param src 文件或文件夹的File对象
	 * @param desPath 目标路径
	 * @see #move(String, String)
	 */
	public static void move(File src, String desPath) {
		move(src.getAbsolutePath(), desPath);
	}

	/**
	 * 重命名文件或文件夹
	 * @param filePath 路径
	 * @param newName 新名称
	 * @apiNote <pre>{@code
	 * // 将a.txt重命名为b.jpg
	 * Files.rename("/path/to/a.txt", "b.jpg");
	 *
	 * // 将a文件夹命名为b
	 * Files.rename("/path/to/a", "b");
	 * }</pre>
	 */
    public static void rename(String filePath, String newName) {
		Objs.throwsIfNullOrEmpty("文件路径和新名称不能为空", filePath, newName);

        // 创建源文件
        File file = new File(filePath);

        // 判断源文件是否存在
        if (!file.exists()) {
            throw new RuntimeException("源文件不存在");
        }

        // 创建新目标文件
        File newFile = new File(file.getParent() + "/" + newName);

        // 重命名文件
        if (!file.renameTo(newFile)) {
			throw new RuntimeException("重命名失败！");
		}

        // 提示信息
        log(() -> "成功将 “" + file.getName() + "” 重命名为 “" + newName + "”");
    }

	/**
	 * 重命名文件或文件夹
	 * @param src 源文件
	 * @param newName 新名称
	 * @see #rename(String, String)
	 */
	public static void rename(File src, String newName) {
		rename(src.getAbsolutePath(), newName);
	}

	/**
	 * 删除文件或文件夹
	 * @param path 路径
	 * @apiNote <pre>{@code
	 * // 删除a.txt
	 * Files.delete("./a.txt");
	 *
	 * // 删除a文件夹
	 * Files.delete("/path/to/a");
	 * }</pre>
	 */
	public static void delete(String path) {
		Objs.throwsIfNullOrEmpty("路径不能为空", path);

        File f = new File(path);

        // 判断要删除的文件是否存在
        if (!f.exists()) {
			throw new RuntimeException("要删除的文件不存在！");
		}

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 判断是否为文件/文件夹
        log(() -> "正在删除 " + f.getName() + " 这个" + (f.isFile() ? "文件" : "文件夹"));

        // 递归删除
		new Object() {
			void loop(File f) {
				// 如果不能直接删除
				if (!f.delete()){
					// 递归删除
					File[] fs = f.listFiles();

					if (fs == null) {
						throw new RuntimeException("展开文件夹失败！");
					}

					for (File del : fs) {
						loop(del);
					}

					// 删除之前的非空文件夹
					if (!f.delete()) {
						throw new RuntimeException("删除失败！");
					}
				} else {
					log(() -> "正在删除：" + f.getAbsolutePath());
				}
			}
		}.loop(f);

        // 统计时间
        long endTime = System.currentTimeMillis();

        log(() -> "耗时" + (double)((endTime - startTime) / 1000) + "秒，已完成");
    }

	/**
	 * 删除文件或文件夹
	 * @param src 源文件的File对象
	 * @see #delete(String)
	 */
	public static void delete(File src) {
		delete(src.getAbsolutePath());
	}

	/**
	 * 删除多个文件或文件夹
	 * @param paths 多个路径
	 * @apiNote <pre>{@code
	 * // 删除a.txt, b文件夹
	 * Files.delete("./a.txt", "/path/to/b");
	 * }</pre>
	 * @since 1.8
	 */
	public static void delete(String... paths) {
		for (var path : paths) {
			delete(path);
		}
	}

	/**
	 * 删除多个文件或文件夹
	 * @param fs 文件或文件夹
	 * @see #delete(String...)
	 */
	public static void delete(File... fs) {
		for (File f : fs) {
			delete(f);
		}
	}

	/**
	 * 删除多个文件或文件夹
	 * @param list 文件列表
	 * @apiNote <pre>{@code
	 * // 文件，文件夹列表（包含a.txt和b文件夹）
	 * var list = List.of(new File("./a.txt"), new File("/path/to/b"));
	 * // 删除列表中的文件/文件夹
	 * Files.delete(list);
	 * }</pre>
	 */
    public static void delete(List<File> list) {
		for (File f : list) {
			delete(f);
		}
    }

	/**
	 * 压缩文件或文件夹
	 * @param srcPath 源路径
	 * @param zipPath zip路径（可为空，默认是源路径）
	 * @param storeMode 是否启用储存模式
	 * @apiNote <pre>{@code
	 * // 将a文件夹压缩为zip文件，生成在当前路径（/path/to/a.zip），不打开储存模式
	 * Files.zip("/path/to/a", null, null);
	 *
	 * // 将a.txt压缩为zip文件，生成在当前路径（/path/to/a.txt.zip），不打开储存模式
	 * Files.zip("./a.txt", null, null);
	 *
	 * // 将a文件夹压缩为zip文件，生成在/path/to/b/a.zip，不打开储存模式
	 * Files.zip("/path/to/a", "/path/to/b", null);
	 *
	 * // 将a文件夹压缩为zip文件，生成在/path/to/b/a.zip，打开储存模式（只固定，实际不压缩）
	 * Files.zip("/path/to/a", "/path/to/b", true);
	 * }</pre>
	 */
    @SuppressWarnings("resource")
	public static void zip(String srcPath, String zipPath, @DefaultArg("false") Boolean storeMode) {
		Objs.throwsIfNullOrEmpty("源路径不能为空", srcPath);

		{
			// 源文件
			var src = new File(srcPath);

			// zipPath默认是源路径
			if (zipPath == null) {
				zipPath = src.getParent() + "/" + src.getName() + ".zip";
			}
		}

		// 源文件
		File src = new File(srcPath);

		// 检查路径
		if (!src.exists()) {
			throw new RuntimeException("源文件不存在");
		}

        // 创建目标zip文件
        File zip = new File(zipPath);

        // 创建目录
        mkdirs(zip.getParentFile());

        // 创建zip输出流
		ZipOutputStream out = Tries.tryReturnsT(() -> new ZipOutputStream(new FileOutputStream(zip)));

		assert out != null;

		// 按照用户需求设置是否为储存模式
		if (storeMode) {
			out.setMethod(ZipOutputStream.STORED);
		}

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 日志信息
        log(() -> "正在压缩 " + src.getName() + " 这个" + (src.isFile() ? "文件" : "文件夹"));

		// 递归创建zip
		new Object() {
			void loop(File f, String root) {
				// 若f是一个文件夹
				if (f.isDirectory()) {
					File[] fs = f.listFiles();
					if (fs == null) {
						throw new RuntimeException("文件夹展开失败！");
					}

					if (fs.length == 0) {
						// 创建(放入)此文件夹
						ZipEntry entry = new ZipEntry(root + "/");
						if (storeMode) {
							entry.setCrc(0);
							entry.setSize(0);
						}
						Tries.tryThis(() -> out.putNextEntry(entry));
					} else {
						for (File src : fs) {
							// 即将被复制文件的完整路径(root为根目录)
							String filePath = root + "/" + src.getName();
							// 递归创建
							loop(src, filePath);
						}
					}
				} else {
					// 提示信息
					log(() -> "正在压缩: " + f.getAbsolutePath());

					// 放入上文提到的完整路径
					try {
						ZipEntry entry = new ZipEntry(root);
						if (storeMode) {
							CRC32 crc = new CRC32();
							var in = new FileInputStream(f);
							byte[] b = new byte[8192];
							int i;
							while ((i = in.read(b)) != -1) {
								crc.update(b, 0, i);
							}
							in.close();

							entry.setCrc(crc.getValue());
							entry.setSize(f.length());
						}
						out.putNextEntry(entry);
					} catch (IOException e) {
						e.printStackTrace();
					}

					// 输出文件到zip流
					try (var in = new FileInputStream(f)) {
						in.transferTo(out);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.loop(src, src.getName());

        // 关闭zip输出流
		Tries.tryThis(out::close);

		long endTime = System.currentTimeMillis();
		log(() -> "耗时" + (double) ((endTime - startTime) / 1000) + "秒，已完成");
    }

	/**
	 * 在本目录下创建压缩文件
	 * @param srcPath 源路径
	 * @param storeMode 是否启用储存模式
	 * @apiNote <pre>{@code
	 * // 压缩a.txt，生成在/path/to/a.txt.zip，不启用储存模式
	 * Files.zip("/path/to/a.txt", false);
	 *
	 * // 压缩a文件夹，生成在/path/to/a.zip，启用储存模式（只固定，实际不压缩）
	 * Files.zip("/path/to/a", true);
	 * }</pre>
	 */
	public static void zip(String srcPath, Boolean storeMode) { zip(srcPath, null, storeMode); }

	/**
	 * 在本目录下创建压缩文件（非储存模式）
	 * @param srcPath 源路径
	 * @apiNote <pre>{@code
	 * // 压缩a.txt，生成在/path/to/a.txt.zip，不启用储存模式
	 * Files.zip("/path/to/a.txt.zip");
	 *
	 * // 压缩a文件夹，生成在/path/to/a.zip，不启用储存模式
	 * Files.zip("/path/to/a");
	 * }</pre>
	 */
	public static void zip(String srcPath) { zip(srcPath, null, null); }

	/**
	 * 压缩文件或文件夹
	 * @param src 源文件
	 * @param zipPath zip路径（可为空，默认是源路径）
	 * @param storeMode 是否启用储存模式（可为空，默认是否）
	 * @see #zip(String, String, Boolean)
	 */
	public static void zip(File src, String zipPath, Boolean storeMode) {
		zip(src.getAbsolutePath(), zipPath, storeMode);
	}

	/**
	 * 在本目录下创建压缩文件
	 * @param src 源文件
	 * @param storeMode 是否启用储存模式
	 * @see #zip(String, Boolean)
	 */
	public static void zip(File src, Boolean storeMode) { zip(src, null, storeMode); }

	/**
	 * 在本目录下创建压缩文件（非储存模式）
	 * @param src 源文件
	 * @see #zip(String)
	 */
	public static void zip(File src) { zip(src, null, null); }

	/**
	 * 解压文件
	 * @param zipPath zip路径
	 * @param desPath 目标路径（可为空，默认是zip所在目录）
	 * @apiNote <pre>{@code
	 * // 解压a.zip到默当前目录（/path/to）
	 * Files.unzip("/path/to/a.zip", null);
	 *
	 * // 解压a.zip到/path/to/b
	 * Files.unzip("/path/to/a.zip", "/path/to/b");
	 * }</pre>
	 */
	@SuppressWarnings("resource")
	public static void unzip(String zipPath, String desPath) {
		Objs.throwsIfNullOrEmpty("zip路径不能为空", zipPath);

		{
			// desPath默认为zip所在目录
			desPath = Optional.ofNullable(desPath).orElse(new File(zipPath).getParent());
		}

        // 创建源文件
        File src = new File(zipPath);
        // 判断是否存在
        if (!src.exists()) {
			throw new RuntimeException("zip文件不存在!");
		}

        // 创建zip文件
        var zip = Tries.tryReturnsT(() -> new ZipFile(src));

        assert zip != null;

        // 创建entries数组
        var enumeration = zip.entries();
        // 线程池
        var pool = Executors.newFixedThreadPool(MAX_OPERATING_AMOUNT);

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 日志信息
        log(() -> "正在解压 " + src.getName() + " 这个压缩文件");

        // 遍历数组
        while (enumeration.hasMoreElements()) {
            // 创建entry
            var entry = enumeration.nextElement();

            // 创建目标文件
            File des = new File(desPath + "/" + entry.getName());

            //如果entry指向一个文件夹
            if (entry.isDirectory()) {
                //若不存在就创建文件夹
				mkdirs(des);
            } else {
				mkdirs(des.getParentFile());
				// 多线程
                pool.execute(() -> {
					// 日志信息
					log(() -> "正在解压: " + entry.getName());

					// 创建父目录
					mkdirs(des.getParentFile());

					try (var in = zip.getInputStream(entry);
						 var out = new FileOutputStream(des)) {
						in.transferTo(out);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
            }
        }
        pool.shutdown();

		// 等待执行完成
		Tries.tryThis(() -> { while (!pool.awaitTermination(1, TimeUnit.DAYS)) {} });

        //关闭流
		Tries.tryThis(zip::close);

		long endTime = System.currentTimeMillis();
		log(() -> "耗时" + (double) ((endTime - startTime) / 1000) + "秒，已完成");
    }

	/**
	 * 在本目录下解压文件
	 * @param zipPath zip文件路径
	 * @apiNote <pre>{@code
	 * // 解压a.zip到/path/to
	 * Files.unzip("/path/to/a.zip");
	 * }</pre>
	 */
	public static void unzip(String zipPath) { unzip(zipPath, null); }

	/**
	 * 解压文件
	 * @param zip zip文件
	 * @param desPath 目标路径
	 * @see #unzip(String, String)
	 */
	public static void unzip(File zip, String desPath) {
		unzip(zip.getAbsolutePath(), desPath);
	}

	/**
	 * 在本目录下解压文件
	 * @param zip zip文件
	 * @see #unzip(String)
	 */
	public static void unzip(File zip) { unzip(zip, null); }

	/**
	 * 将默认参数（字符串）转化为Charset对象的转换类
	 * @since 3.0
	 */
	private static class StringToCharset extends DefaultArgTransfer<Charset> {
		@Override
		public Charset transfer(String defaultArg) {
			return Charset.forName(defaultArg);
		}
	}

	/**
	 * 读取文件到字符串
	 * @param path 源文件路径
	 * @param cs 文件编码
	 * @return 结果字符串
	 * @apiNote <pre>{@code
	 * // 使用默认编码（UTF-8）读取a.txt到结果字符串
	 * String res = Files.read("./a.txt", null);
	 * // 使用GBK编码读取a.txt到结果字符串
	 * String res2 = Files.read("./a.txt", Charset.forName("GBK"));
	 * }</pre>
	 */
	public static String read(String path,
							  @DefaultArg(value = "UTF-8", transferClass = StringToCharset.class) Charset cs) {
		return new String(readToBytes(path), cs);
	}

	/**
	 * 使用UTF-8编码读取文件到字符串
	 * @param path 源文件路径
	 * @return 结果字符串
	 * @apiNote <pre>{@code
	 * // 使用默认编码（UTF-8）读取a.txt到结果字符串
	 * String res = Files.read("./a.txt");
	 * }</pre>
	 */
	public static String read(String path) { return read(path, null); }

	/**
	 * 读取文件到字符串
	 * @param src 源文件
	 * @param cs 文件编码（可为空，默认是UTF-8）
	 * @return 结果字符串
	 * @see #read(String, Charset)
	 */
	public static String read(File src, Charset cs) {
		return read(src.getAbsolutePath(), cs);
	}

	/**
	 * 使用UTF-8编码读取文件到字符串
	 * @param src 源文件
	 * @return 结果字符串
	 * @see #read(String)
	 */
	public static String read(File src) { return read(src, null); }


	/**
	 * 读取文件到byte数组
	 * @param path 源路径
	 * @return 结果数组
	 * @apiNote <pre>{@code
	 * // 读取a.txt到byte数组
	 * var bs = Files.readToBytes("./a.txt");
	 * }</pre>
	 */
	public static byte[] readToBytes(String path) {
		var src = new File(path);

		// 文件必须存在
		if (!src.exists()) {
			throw new RuntimeException("文件不存在");
		}

		// 只能读取文件
		if (src.isDirectory()) {
			throw new RuntimeException("只能读取文件！");
		}

		var out = new ByteArrayOutputStream();
		try (var in = new FileInputStream(src)) {
			in.transferTo(out);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return out.toByteArray();
	}

	/**
	 * 读取文件到byte数组
	 * @param src 源文件
	 * @return 结果数组
	 * @see #readToBytes(String)
	 */
	public static byte[] readToBytes(File src) {
		return readToBytes(src.getAbsolutePath());
	}

	/**
	 * 列出一个目录的一级文件/文件夹
	 * @param path 文件夹路径
	 * @return 结果列表
	 * @apiNote <pre>{@code
	 * // 列出a目录的一级文件/文件夹到列表中
	 * var list = Files.list("/path/to/a");
	 * }</pre>
	 * @since 2.9
	 */
	public static List<File> list(String path) {
		Objs.throwsIfNullOrEmpty(path);

		var dir = new File(path);

		if (!dir.exists()) {
			throw new RuntimeException("文件夹不存在：\"%s\"".formatted(dir.getAbsolutePath()));
		}

		if (dir.isFile()) {
			throw new RuntimeException("不能传入文件的路径：\"%s\"".formatted(dir.getAbsolutePath()));
		}

		return Optional.ofNullable(dir.listFiles())
				.map(Arrays::stream)
				.map(Stream::toList)
				.orElse(List.of());
	}

	/**
	 * 列出一个目录的一级文件/文件夹
	 * @param dir 文件夹File对象
	 * @return 结果列表
	 * @see #list(String)
	 * @since 2.9
	 */
	public static List<File> list(File dir) {
		return list(dir.getAbsolutePath());
	}

	/**
	 * 读取文件中的所有行到字符串流
	 * @param path 文件路径
	 * @param cs 文件编码
	 * @return 所有行集合
	 * @apiNote <pre>{@code
	 * // 获取a.txt所有行到Stream（使用默认UTF-8编码）并打印
	 * Files.lines("./a.txt", null)
	 *      .forEach(System.out::println);
	 *
	 * // 获取a.txt所有行到Stream（使用GBK编码）并打印
	 * Files.lines("./a.txt", Charset.forName("GBK"))
	 *      .forEach(System.out::println);
	 * }</pre>
	 * @since 1.8
	 */
	public static Stream<String> lines(String path,
									   @DefaultArg(value = "UTF-8", transferClass = StringToCharset.class) Charset cs) {
		var content = read(path, cs);
		return Arrays.stream(content.split("\n"));
	}

	/**
	 * 使用UTF-8编码读取文件中的所有行到字符串流
	 * @param path 文件路径
	 * @return 所有行集合
	 * @apiNote <pre>{@code
	 * // 读取a.txt所有行到Stream（使用UTF-8编码）并打印
	 * Files.lines("./a.txt")
	 *      .forEach(System.out::println);
	 * }</pre>
	 * @since 1.8
	 */
	public static Stream<String> lines(String path) { return lines(path, null); }

	/**
	 * 读取文件中的所有行到字符串流
	 * @param src 文件对象
	 * @param cs 文件编码（可为空，默认是UTF-8编码）
	 * @return 所有行集合
	 * @since 1.8
	 * @see #lines(String, Charset)
	 */
	public static Stream<String> lines(File src, Charset cs) {
		return lines(src.getAbsolutePath(), cs);
	}

	/**
	 * 使用UTF-8编码读取文件中的所有行到字符串流
	 * @param src 文件对象
	 * @return 所有行集合
	 * @since 1.8
	 * @see #lines(String)
	 */
	public static Stream<String> lines(File src) { return lines(src, null); }

	/**
	 * 把byte数组输出到文件
	 * @param bs byte数组
	 * @param desPath 目标文件路径
	 * @param appendMode 是否为追加模式
	 * @apiNote <pre>{@code
	 * // 将byte数组bs输出到a.txt中（非追加模式）
	 * Files.out(bs, "./a.txt", null);
	 *
	 * // 将byte数组bs输出到a.txt中（追加模式）
	 * Files.out(bs, "./a.txt", true);
	 * }</pre>
	 */
	public static void out(byte[] bs, String desPath, @DefaultArg("false") Boolean appendMode) {
		Objs.throwsIfNullOrEmpty("byte数组和目标文件路径不能为空", bs, desPath);

		var des = new File(desPath);

		// 创建父目录
		mkdirs(des.getParentFile());

		try (var out = new FileOutputStream(des, appendMode)) {
			out.write(bs);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用非追加模式把byte数组输出到文件
	 * @param bs byte数组
	 * @param desPath 目标文件路径
	 * @apiNote <pre>{@code
	 * // 将byte数组bs输出到a.txt（非追加模式）
	 * Files.out(bs, "./a.txt");
	 * }</pre>
	 */
	public static void out(byte[] bs, String desPath) { out(bs, desPath, null); }

	/**
	 * 把byte数组输出到文件
	 * @param bs byte数组
	 * @param desFile 目标文件
	 * @param appendMode 是否为追加模式（可为空，默认否）
	 * @see #out(byte[], String, Boolean)
	 */
	public static void out(byte[] bs, File desFile, Boolean appendMode) {
		out(bs, desFile.getAbsolutePath(), appendMode);
	}

	/**
	 * 用非追加模式把byte数组输出到文件
	 * @param bs byte数组
	 * @param desFile 目标文件
	 * @see #out(byte[], String)
	 */
	public static void out(byte[] bs, File desFile) { out(bs, desFile, null); }

	/**
	 * 输出字符串到文件
	 * @param s 字符串
	 * @param desPath 文件路径
	 * @param charset 目标文件编码
	 * @param appendMode 是否用追加模式
	 * @apiNote <pre>{@code
	 * // 输出hello到a.txt（默认UTF-8编码，非追加模式）
	 * Files.out("hello", "./a.txt", null, null);
	 *
	 * // 输出hello到a.txt（GBK编码，追加模式）
	 * Files.out("hello", "./a.txt", Charset.forName("GBK"), true);
	 * }</pre>
	 */
	public static void out(String s,
						   String desPath,
						   @DefaultArg(value = "UTF-8", transferClass = StringToCharset.class) Charset charset,
						   @DefaultArg("false") Boolean appendMode) {
		out(s.getBytes(charset), desPath, appendMode);
	}

	/**
	 * 使用UTF-8编码输出字符串到文件
	 * @param s 字符串
	 * @param desPath 文件路径
	 * @param appendMode 是否用追加模式
	 * @apiNote <pre>{@code
	 * // 使用UTF-8编码输出hello到a.txt（非追加模式）
	 * Files.out("hello", "./a.txt", null);
	 *
	 * // 使用UTF-8编码输出hello到a.txt（追加模式）
	 * Files.out("hello", "./a.txt", true);
	 * }</pre>
	 */
	public static void out(String s, String desPath, Boolean appendMode) { out(s, desPath, null, appendMode); }

	/**
	 * 使用非追加模式和UTF-8编码输出字符串到文件
	 * @param s 字符串
	 * @param desPath 文件路径
	 * @apiNote <pre>{@code
	 * // 使用UTF-8编码，非追加模式将hello输出到a.txt
	 * Files.out("hello", "./a.txt");
	 * }</pre>
	 */
	public static void out(String s, String desPath) { out(s, desPath, null, null); }

	/**
	 * 输出字符串到文件
	 * @param s 字符串
	 * @param des 目标文件
	 * @param charset 目标文件编码名称（可为空，默认UTF-8编码）
	 * @param appendMode 是否用追加模式（可为空，默认否）
	 * @see #out(String, String, Charset, Boolean)
	 */
	public static void out(String s, File des, Charset charset, Boolean appendMode) {
		out(s, des.getAbsolutePath(), charset, appendMode);
	}

	/**
	 * 使用UTF-8编码输出字符串到文件
	 * @param s 字符串
	 * @param des 目标文件
	 * @param appendMode 是否用追加模式
	 * @see #out(String, String, Boolean)
	 */
	public static void out(String s, File des, Boolean appendMode) { out(s, des, null, appendMode); }

	/**
	 * 使用非追加模式和UTF-8编码输出字符串到文件
	 * @param s 字符串
	 * @param des 目标文件
	 * @see #out(String, String)
	 */
	public static void out(String s, File des) { out(s, des, null, null); }

	/**
	 * 输入流输出到文件
	 * @param in 输入流
	 * @param path 文件路径
	 * @param appendMode 是否用追加模式
	 * @apiNote <pre>{@code
	 * // 将输入流in输出到a.txt中（非追加模式）
	 * Files.out(in, "./a.txt", null);
	 *
	 * // 将输入流in输出到a.txt中（追加模式）
	 * Files.out(in, "./a.txt", true);
	 * }</pre>
	 */
	public static void out(InputStream in, String path, boolean appendMode) {
		Objs.throwsIfNullOrEmpty("输入流和文件路径不能为空", in, path);

		mkdirs(new File(path).getParentFile());

		try (in; var out = new FileOutputStream(path, appendMode)) {
			in.transferTo(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输入流输出到文件
	 * @param in 输入流
	 * @param des 文件File对象
	 * @param appendMode 是否用追加模式
	 * @see #out(InputStream, String, boolean)
	 */
	public static void out(InputStream in, File des, boolean appendMode) {
		out(in, des.getAbsolutePath(), appendMode);
	}


	// endregion

	// region attr

	/**
	 * 文件长度转换
	 * @param length 文件字节大小
	 * @return 格式化后的文件长度
	 * @apiNote <pre>{@code
	 * // 将100000字节转换成人类可读文件大小 -> "2KB"
	 * String res = Files.lengthFormatter(2048);
	 * }</pre>
	 */
	public static String lengthFormatter(long length) {
		long B = 1, KB = B * 1024, MB = KB * 1024,
				GB = MB * 1024, TB = GB * 1024,
				PB = TB * 1024, EB = PB * 1024;

		// 以下代码由ChatGPT生成
		String[] units = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
		int idx = 0;
		double size = length;

		while (size >= 1024 && idx < units.length - 1) {
			size /= 1024;
			idx++;
		}

		return new DecimalFormat("#.##").format(size) + units[idx];
	}

	/**
	 * 创建新文件
	 * @param path 新文件路径
	 * @since 1.8
	 * @apiNote <pre>{@code
	 * // 创建a.txt（父目录可以不存在）
	 * Files.createFile("/path/to/what/a.txt");
	 * }</pre>
	 */
	public static void createFile(String path) {
		Objs.throwsIfNullOrEmpty("文件路径不能为空！", path);

		var f = new File(path);

		if (!f.exists()) {
			File parentFile;
			if (!(parentFile = f.getParentFile()).exists()) {
				mkdirs(parentFile);
			}

			if (Boolean.FALSE.equals(Tries.tryReturnsT(f::createNewFile))) {
				throw new RuntimeException("文件创建失败!");
			}
		}
	}

	/**
	 * 创建新文件
	 * @param f 文件对象
	 * @since 1.8
	 * @see #createFile(String)
	 */
	public static void createFile(File f) {
		Objs.throwsIfNullOrEmpty(f);
		createFile(f.getAbsolutePath());
	}

	/**
	 * 创建临时文件（名称是时间戳）
	 * @return 临时文件的绝对路径
	 * @since 3.3
	 * @apiNote <pre>{@code
	 * // 创建临时文件，并接收临时文件的绝对路径
	 * var filePath = Files.createTempFile();
	 * }</pre>
	 */
	public static String createTempFile() {
		return createTempFile(String.valueOf(System.nanoTime()));
	}

	/**
	 * 创建临时文件
	 * @param fileName 文件名称
	 * @return 临时文件的绝对路径
	 * @since 3.3
	 * @apiNote <pre>{@code
	 * // 创建a.txt，并接收临时文件的绝对路径
	 * var filePath = Files.createTempFile("a.txt");
	 * }</pre>
	 */
	public static String createTempFile(String fileName) {
		// 获取系统缓存目录
		var tempDir = System.getProperty("java.io.tmpdir");

		var filePath = tempDir + File.separator + fileName;
		createFile(filePath);

		return filePath;
	}

	/**
	 * 创建临时文件夹（名称是时间戳）
	 * @return 临时文件夹的绝对路径
	 * @since 3.3
	 * @apiNote <pre>{@code
	 * // 创建临时文件夹，并接收它的绝对路径
	 * var dirPath = Files.createTempDir();
	 *
	 * // 在临时文件夹中创建文件a.txt
	 * Files.createFile(dirPath + "/a.txt");
	 * }</pre>
	 */
	public static String createTempDir() {
		return createTempDir(String.valueOf(System.nanoTime()));
	}

	/**
	 * 创建临时文件夹
	 * @param dirName 文件夹名称
	 * @return 临时文件夹的绝对路径
	 * @since 3.3
	 * @apiNote <pre>{@code
	 * // 创建临时文件夹dir，并接收它的绝对路径
	 * var dirPath = Files.createTempDir("dir");
	 *
	 * // 在临时文件夹dir中创建文件a.txt
	 * Files.createFile(dirPath + "/a.txt");
	 * }</pre>
	 */
	public static String createTempDir(String dirName) {
		// 获取系统缓存目录
		var tempDir = System.getProperty("java.io.tmpdir");

		var dirPath = tempDir + File.separator + dirName;
		mkdirs(dirPath);

		return dirPath;
	}

	/**
	 * 创建多级目录
	 * @param path 多级目录路径
	 * @apiNote <pre>{@code
	 * // 创建a/b/c目录
	 * Files.mkdirs("/path/to/a/b/c");
	 * }</pre>
	 */
	public static void mkdirs(String path) {
		Objs.throwsIfNullOrEmpty("目录路径不能为空！", path);

		var dir = new File(path);

		if (dir.exists()) {
			return;
		}

		if (!dir.mkdirs()) {
			throw new RuntimeException("创建多级目录失败！");
		}
	}

	/**
	 * 创建多级目录
	 * @param dir 多级目录File对象
	 * @see #mkdirs(String)
	 */
	public static void mkdirs(File dir) {
		mkdirs(dir.getAbsolutePath());
	}

	/**
	 * 判断文件/文件夹是否存在
	 * @param path 文件/文件夹路径
	 * @return 是否存在
	 * @apiNote <pre>{@code
	 * // 判断a.txt是否存在
	 * boolean res = Files.exists("/path/to/a.txt");
	 * }</pre>
	 * @since 1.8
	 */
	public static boolean exists(String path) {
		return path != null && new File(path).exists();
	}

	/**
	 * 判断路径是否存在
	 * @param f 路径的File对象
	 * @return 是否存在
	 * @since 1.8
	 * @see #exists(String)
	 */
	public static boolean exists(File f) {
		return f != null && exists(f.getAbsolutePath());
	}

	/**
	 * 判断路径是否是文件
	 * @param path 文件路径
	 * @return 是否是文件
	 * @apiNote <pre>{@code
	 * // 判断a.txt是否是文件
	 * boolean res = Files.isFile("/path/to/a.txt");
	 * }</pre>
	 * @since 3.6
	 */
	public static boolean isFile(String path) {
		return exists(path) && new File(path).isFile();
	}

	/**
	 * 判断路径是否是文件
	 * @param f 路径的File对象
	 * @return 是否是文件
	 * @since 3.6
	 * @see #isFile(String)
	 */
	public static boolean isFile(File f) {
		return f != null && isFile(f.getAbsolutePath());
	}

	/**
	 * 判断路径是否是文件夹
	 * @param path 文件路径
	 * @return 是否是文件夹
	 * @apiNote <pre>{@code
	 * // 判断a是否是文件夹
	 * boolean res = Files.isDir("/path/to/a");
	 * }</pre>
	 * @since 3.6
	 */
	public static boolean isDir(String path) {
		return exists(path) && new File(path).isDirectory();
	}

	/**
	 * 判断路径是否是文件夹
	 * @param f 路径的File对象
	 * @return 是否是文件夹
	 * @since 3.6
	 * @see #isDir(String)
	 */
	public static boolean isDir(File f) {
		return f != null && isDir(f.getAbsolutePath());
	}

	/**
	 * 文件信息
	 * @param path 文件/文件夹路径
	 * @return FileInfo对象
	 * @apiNote <pre>{@code
	 * // 获取a目录的信息
	 * FileInfo info = Files.getFileInfo("/path/to/a");
	 * long fileCount = fileInfo.fileCount();
	 * long dirCount = fileInfo.dirCount();
	 * long totalLength = fileInfo.totalLength();
	 *
	 * // 获取b.txt文件的信息
	 * FileInfo info2 = Files.getFileInfo("./b.txt");
	 * long
	 * }</pre>
	 * @since 3.6
	 */
	public static FileInfo getFileInfo(String path) {
		// 检查路径是否存在
		if (!exists(path)) {
			throw new RuntimeException("路径不存在！");
		}

		return FileInfo.of(path);
	}

	/**
	 * 文件信息
	 * @param src 文件/文件夹的File对象
	 * @return FileInfo对象
	 * @since 3.6
	 * @see #getFileInfo(String)
	 */
	public static FileInfo getFileInfo(File src) {
		Objs.throwsIfNullOrEmpty(src);
		return getFileInfo(src.getAbsolutePath());
	}

	/**
	 * 文件详细信息类
	 * @param fileCount 文件数量
	 * @param dirCount 文件夹数量
	 * @param totalLength 文件总长度
	 * @since 3.6
	 */
	public record FileInfo(long fileCount, long dirCount, long totalLength) {
		/**
		 * 创建文件详细信息类实例
		 * @param path 文件/文件夹路径
		 * @return 创建的实例
		 */
		private static FileInfo of(String path) {
			// 如果是文件直接返回
			if (isFile(path)) {
				return new FileInfo(1L, 0L, getLength(path));
			}

			// 文件夹临时列表
			var tempDirList = new ArrayList<>(List.of(new File(path)));

			// 文件数量，文件夹数量和文件总长度
			long fileCount = 0, dirCount = 0, totalLength = 0;

			// 不断遍历文件夹临时列表
			while (!tempDirList.isEmpty()){
				var dir = tempDirList.remove(0);

				var list = dir.listFiles();
				if (list == null) {
					// 打印错误信息
					System.err.println("展开文件夹失败：" + dir.getAbsolutePath());
					continue;
				}

				for (var f : list) {
					if (f.isDirectory()){
						dirCount++;
						tempDirList.add(f);
					} else {
						fileCount++;
						totalLength += f.length();
					}
				}
			}
			return new FileInfo(fileCount, dirCount, totalLength);
		}
	}

	/**
	 * 获取文件/文件夹的字节长度
	 * @param path 文件/文件夹路径
	 * @return 文件/文件夹字节长度
	 * @apiNote <pre>{@code
	 * // 获取a.jpg的文件字节长度
	 * long length = Files.getLength("/path/to/a.jpg");
	 * // 获取b文件夹的字节总长度
	 * long length = Files.getLength("/path/to/b");
	 * }</pre>
	 * @since 3.6
	 */
	public static long getLength(String path) {
		// 判断路径是否存在
		if (!exists(path)) {
			throw new RuntimeException("文件不存在：" + path);
		}

		// 返回文件/文件夹长度
		return isFile(path) ? new File(path).length() : getFileInfo(path).totalLength();
	}

	/**
	 * 获取指定路径的父路径
	 * @param path 指定路径
	 * @return 父路径
	 * @apiNote <pre>{@code
	 * // 获取a.txt的父路径
	 * var parentPath = Files.getParentPath("/path/to/a.txt");
	 * }</pre>
	 * @since 3.4
	 */
	public static String getParentPath(String path) {
		return new File(path).getParent();
	}

	/**
	 * 从相对路径获取绝对路径
	 * @param relativePath 相对路径
	 * @return 绝对路径字符串
	 * @apiNote <pre>{@code
	 * // 获取a.txt的绝对路径
	 * var absolutePath = Files.getAbsolutePath("./a.txt");
	 * }</pre>
	 * @since 3.2
	 */
	public static String getAbsolutePath(String relativePath) {
		var f = new File(relativePath);
		if (!f.exists()) {
			throw new RuntimeException("路径不存在！");
		}

		return f.getAbsolutePath();
	}

	/**
	 * 获取文件的编码字符集
	 * @param path 文件路径
	 * @return 编码字符集
	 * @apiNote <pre>{@code
	 * // 获取a.txt的编码字符集
	 * Charset c = Files.getEncoding("./a.txt").orElseThrow();
	 * }</pre>
	 * @since 2.7
	 */
	public static Optional<Charset> getEncoding(String path) {
		Objs.throwsIfNullOrEmpty("文件路径不能为空！", path);
		var f = new File(path);

		if (!f.exists()) {
			throw new RuntimeException("文件不存在！");
		}

		if (f.isDirectory()) {
			throw new RuntimeException("非法参数：传入文件夹路径");
		}

		// 找到的文件编码
		Charset encoding;

		// 判断有bom的文件
		try (var in = new FileInputStream(f)) {
			// 获取文件前两个字节（bom）
			var bom = (in.read() << 8) + in.read();

			encoding = switch (bom) {
				case 0xefbb -> StandardCharsets.UTF_8;
				case 0xfeff -> StandardCharsets.UTF_16BE;
				case 0xfffe -> StandardCharsets.UTF_16LE;
				default -> null;
			};
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// 判断无bom的文件
		if (encoding == null) {
			var bytes = readToBytes(f);
			// 尝试用utf-8解码为字符串并编码为gbk字节数组
			var utf8Read2GbkBytes = new String(bytes, StandardCharsets.UTF_8)
					.getBytes(Charset.forName("GBK"));

			// 尝试使用gbk解码为字符串并编码回utf-8字节数组
			var desGbkRead2Utf8Bytes = new String(utf8Read2GbkBytes, Charset.forName("GBK"))
					.getBytes(StandardCharsets.UTF_8);

			// 尝试用gbk解码为字符串并编码为utf-8字节数组
			var gbkRead2Utf8Bytes = new String(bytes, Charset.forName("GBK"))
					.getBytes(StandardCharsets.UTF_8);

			// 尝试使用utf-8解码为字符串并编码回gbk字节数组
			var desUtf8ReadGbkBytes = new String(gbkRead2Utf8Bytes, StandardCharsets.UTF_8)
					.getBytes(Charset.forName("GBK"));

			// 如果经过中间转换后仍和原bytes相同，就找到了字符集
			if (Arrays.equals(bytes, desGbkRead2Utf8Bytes)) {
				encoding = StandardCharsets.UTF_8;
			} else if (Arrays.equals(bytes, desUtf8ReadGbkBytes)) {
				encoding = Charset.forName("GBK");
			}
		}

		return Optional.ofNullable(encoding);
	}

	/**
	 * 获取文件的编码字符集
	 * @param f 文件对象
	 * @return 编码字符集
	 * @since 2.7
	 * @see #getEncoding(String)
	 */
	public static Optional<Charset> getEncoding(File f) {
		Objs.throwsIfNullOrEmpty(f);
		return getEncoding(f.getAbsolutePath());
	}

	/**
	 * 设置文件编码（自动识别原编码）
	 * @param path 源文件路径
	 * @param newCharset 新编码
	 * @apiNote <pre>{@code
	 * // 设置a.txt的编码为GBK
	 * Files.setEncoding("./a.txt", Charset.forName("GBK"));
	 * }</pre>
	 * @since 2.7
	 */
	public static void setEncoding(String path, Charset newCharset) {
		var oldCharset = getEncoding(path).orElseThrow(() -> new RuntimeException("无法识别文件编码！"));

		setEncoding(path, oldCharset, newCharset);
	}

	/**
	 * 设置文件编码（自动识别原编码）
	 * @param src 源文件
	 * @param newCharset 新编码名称
	 * @since 2.7
	 * @see #setEncoding(String, Charset)
	 */
	public static void setEncoding(File src, Charset newCharset) {
		setEncoding(src.getAbsolutePath(), newCharset);
	}

	/**
	 * 设置文件编码
	 * @param path 源文件路径
	 * @param oldCharset 旧编码
	 * @param newCharset 新编码
	 * @apiNote <pre>{@code
	 * // 将原来UTF-8编码的a.txt设置为GBK编码
	 * Files.setEncoding("./a.txt", StandardCharsets.UTF_8, Charset.forName("GBK"));
	 * }</pre>
	 */
	public static void setEncoding(String path, Charset oldCharset, Charset newCharset) {
		// 如果新旧编码相同就直接返回
		if (oldCharset.equals(newCharset)) {
			return;
		}

		var src = new File(path);
		byte[] srcBytes = readToBytes(src);
		byte[] bs = new String(srcBytes, oldCharset).getBytes(newCharset);
		out(bs, src);
	}

	/**
	 * 设置文件编码
	 * @param src 源文件
	 * @param oldCharset 旧编码名称
	 * @param newCharset 新编码名称
	 * @see #setEncoding(String, Charset, Charset)
	 */
	public static void setEncoding(File src, Charset oldCharset, Charset newCharset) {
		setEncoding(src.getAbsolutePath(), oldCharset, newCharset);
	}

	/**
	 * 按时间重命名（升序）
	 * @param dirPath 搜索文件夹路径
	 * @param suffix 后缀（含小数点）
	 * @apiNote <pre>{@code
	 * // 将a文件夹中一级目录所有文件按照时间重命名为jpg类型
	 * // 时间越早序号越小，重命名结果是：1.jpg, 2.jpg, ...
	 * Files.renameByTime("/path/to/a", ".jpg");
	 * }</pre>
	 */
	public static void renameByTime(String dirPath, String suffix) {
		Objs.throwsIfNullOrEmpty("文件夹对象和后缀不能为空", dirPath, suffix);

		File[] fs = new File(dirPath).listFiles(File::isFile);
		if (fs == null) {
			return;
		}

		Arrays.sort(fs, Comparator.comparingLong(File::lastModified));

		for (int i = 0; i < fs.length; i++) {
			rename(fs[i], (i + 1) + suffix);
		}
	}

	/**
	 * 按时间重命名
	 * @param dir 搜索文件夹对象
	 * @param suffix 后缀（含小数点）
	 * @see #renameByTime(String, String)
	 */
	public static void renameByTime(File dir, String suffix) {
		renameByTime(dir.getAbsolutePath(), suffix);
	}

	// endregion

	// region find

	/**
	 * 搜索结果类
	 * @param files 结果文件列表
	 * @param dirs 结果文件夹列表
	 */
	public record FindResult(List<File> files, List<File> dirs) {}

	/**
	 * 搜索路径下符合要求的所有文件和文件夹
	 * @param dirPath 搜索文件夹路径
	 * @param filter 文件过滤器
	 * @return 含结果文件和文件夹的集合
	 * @apiNote <pre>{@code
	 * // 在a目录中查找名称中包含jmc的文件/文件夹
	 * var findResult = Files.findAll("/path/to/a", f.getName().contains("jmc"));
	 * // 获取文件列表
	 * var files = findResult.files();
	 * // 获取文件夹列表
	 * var dirs = findResult.dirs();
	 * }</pre>
	 */
	public static FindResult findAll(String dirPath, FileFilter filter) {
		File src = new File(dirPath);
		if (!src.exists()) {
			throw new RuntimeException("路径不存在！");
		}
		if (src.isFile()) {
			throw new RuntimeException("搜索父目录必须为文件夹！");
		}

		// 搜索文件存放集合
		var fileList = new LinkedList<File>();
		// 搜索文件夹存放集合
		var dirList = new LinkedList<File>();

		// 递归查找文件/文件夹
		new Object() {
			void loop(File src) {
				File[] fs = src.listFiles();
				if (fs == null) {
					return;
				}

				for (File f : fs) {
					if (f.isFile()) {
						if (filter.accept(f)) {
							fileList.add(f);
						}
					} else {
						if (filter.accept(f)) {
							dirList.add(f);
						}
						loop(f);
					}
				}
			}
		}.loop(src);

		return new FindResult(fileList, dirList);
	}

	/**
	 * 搜索路径下符合要求的所有文件和文件夹
	 * @param dirPath 搜索文件夹路径
	 * @param contains 文件或文件夹名称中包含的内容
	 * @return 含结果文件和文件夹的集合
	 * @apiNote <pre>{@code
	 * // 在a目录中查找名称包含jmc或者utils的文件/文件夹
	 * var findResult = Files.findAll("/path/to/a", "jmc", "utils");
	 * // 获取文件列表
	 * var files = findResult.files();
	 * // 获取文件夹列表
	 * var dirs = findResult.dirs();
	 * }</pre>
	 */
	public static FindResult findAll(String dirPath, String... contains) {
		FileFilter filter = f -> Strs.orContains(f.getName(),
				contains.length == 0 ? new String[] {""} : contains);
		return Files.findAll(dirPath, filter);
	}

	/**
	 * 搜索路径下符合要求的所有文件
	 * @param dirPath 搜索文件夹路径
	 * @param filter 文件过滤器
	 * @return 含结果文件的列表
	 * @apiNote <pre>{@code
	 * // 在a目录中查找名称包含jmc的文件
	 * List<File> res = Files.findFiles("/path/to/a", f -> f.getName().contains("jmc"));
	 * }</pre>
	 */
	public static List<File> findFiles(String dirPath, FileFilter filter) {
		return findAll(dirPath, filter).files();
	}

	/**
	 * 搜索路径下符合要求的所有文件
	 * @param dirPath 搜索文件夹路径
	 * @param contains 文件名称中包含的内容
	 * @return 含结果文件的列表
	 * @apiNote <pre>{@code
	 * // 在a目录中查找名称包含jmc或者utils的文件
	 * List<File> res = Files.findFiles("/path/to/a", "jmc", "utils");
	 * }</pre>
	 */
	public static List<File> findFiles(String dirPath, String... contains) {
		return findAll(dirPath, contains).files();
	}

	/**
	 * 搜索路径下符合要求的所有文件夹
	 * @param dirPath 搜索文件夹路径
	 * @param filter 文件过滤器
	 * @return 含结果文件夹的列表
	 * @apiNote <pre>{@code
	 * // 在a目录中查找名称包含jmc的文件夹
	 * List<File> res = Files.findDirs("/path/to/a", f -> f.getName().contains("jmc"));
	 * }</pre>
	 */
	public static List<File> findDirs(String dirPath, FileFilter filter) {
		return findAll(dirPath, filter).dirs();
	}

	/**
	 * 搜索路径下符合要求的所有文件夹
	 * @param dirPath 搜索文件夹路径
	 * @param contains 文件夹名称中包含的内容
	 * @return 含结果文件的列表
	 * @apiNote <pre>{@code
	 * // 在a目录中查找名称包含jmc或者utils的文件夹
	 * List<File> res = Files.findDirs("/path/to/a", "jmc", "utils");
	 * }</pre>
	 */
	public static List<File> findDirs(String dirPath, String... contains) {
		return findAll(dirPath, contains).dirs();
	}

	/**
	 * 搜索单个文件
	 * @param path 搜索文件夹路径
	 * @param contains 文件名称包含内容
	 * @return 搜索结果
	 * @apiNote <pre>{@code
	 * // 在a目录中查找名称中包含jmc的任意一个文件
	 * File res = findAny("/path/to/a", "jmc");
	 * }</pre>
	 */
	public static File findAny(String path, String contains) {
		var dir = new File(path);

		if (!dir.exists()) {
			throw new RuntimeException("路径不存在!");
		}

		if (dir.isFile()) {
			throw new RuntimeException("搜索父目录必须为文件夹！");
		}

		// 文件夹临时列表
		var temp = new ArrayList<File>() {{ add(dir); }};

		while (!temp.isEmpty()) {
			var list = temp.remove(0).listFiles();
			if (list == null) {
				return null;
			}

			for (File f : list) {
				if (f.isDirectory()){
					temp.add(f);
				} else {
					if (f.getName().contains(contains)) {
						return f;
					}
				}
			}
		}

		return null;
	}

	/**
	 * 搜索单个文件
	 * @param dir 搜索的文件夹
	 * @param contains 文件名称包含内容
	 * @return 搜索结果
	 * @see #findAny(String, String)
	 */
	public static File findAny(File dir, String contains) {
		return findAny(dir.getAbsolutePath(), contains);
	}

	// endregion

	// region tree

	/**
	 * 树状图统计文件夹
	 * @param path 统计文件夹路径
	 * @param depth 搜索深度
	 * @param minMBSize 结果中的文件/文件夹最小多少MB
	 * @return 文件树对象
	 * @apiNote <pre>{@code
	 * // 获取a目录的文件和文件夹树状图，搜索深度是3，结果包含的文件/文件夹最小的大小为50MB
	 * var fileTree = Files.tree("/path/to/a", 3, 50);
	 * // 打印树状图
	 * System.out.println(fileTree);
	 * }</pre>
	 */
	public static FileTree tree(String path, int depth, double minMBSize) {
		var dir = new File(path);

		if (!dir.exists()) {
			throw new RuntimeException("文件夹不存在");
		}

		if (dir.isFile()) {
			throw new RuntimeException("统计的对象不能为文件！");
		}

		long MIN_LENGTH = (long) (minMBSize * 1024 * 1024);

		return FileTree.getInstance(dir, depth, MIN_LENGTH);
	}

	/**
	 * 树状图统计文件夹
	 * @param dir 搜索文件夹
	 * @param depth 搜索深度
	 * @param minMBSize 结果中的文件/文件夹最小多少MB
	 * @return 文件树对象
	 * @see #tree(String, int, double)
	 */
	public static FileTree tree(File dir, int depth, double minMBSize) {
		return tree(dir.getAbsolutePath(), depth, minMBSize);
	}

	/**
	 * 普通树状图（搜索深度5层，结果中文件/文件夹最小大小为50MB）
	 * @param path 搜索文件夹路径
	 * @return 文件树对象
	 * @apiNote <pre>{@code
	 * // 获取a目录的文件和文件夹的普通树状图（搜索深度5层，结果中文件/文件夹最小大小为50MB）
	 * var fileTree = Files.normalTree("/path/to/a");
	 * // 打印树状图
	 * System.out.println(fileTree);
	 * }</pre>
	 */
	public static FileTree normalTree(String path) {
		return tree(path, 5, 50);
	}

	/**
	 * 普通树状图（搜索深度5层，结果中文件/文件夹最小大小为50MB）
	 * @param dir 搜索文件夹
	 * @return 文件树对象
	 * @see #normalTree(String)
	 */
	public static FileTree normalTree(File dir) {
		return normalTree(dir.getAbsolutePath());
	}

	/**
	 * 单层树状图（结果中文件/文件夹最小大小为50MB）
	 * @param path 搜索文件夹路径
	 * @return 文件树对象
	 * @apiNote <pre>{@code
	 * // 获取a目录的文件和文件夹的单层树状图（结果中文件/文件夹最小大小为50MB）
	 * var fileTree = Files.singleTree("/path/to/a");
	 * // 打印树状图
	 * System.out.println(fileTree);
	 * }</pre>
	 */
	public static FileTree singleTree(String path) {
		return tree(path, 1, 50);
	}

	/**
	 * 单层树状图（结果中文件/文件夹最小大小为50MB）
	 * @param dir 搜索文件夹
	 * @return 文件树对象
	 * @see #singleTree(String)
	 */
	public static FileTree singleTree(File dir) {
		return singleTree(dir.getAbsolutePath());
	}

	/**
	 * 完整树状图（层数、文件/文件夹大小无限制）
	 * @param path 搜索文件夹路径
	 * @return 文件树对象
	 * @apiNote <pre>{@code
	 * // 获取a目录的文件和文件夹的完整树状图（层数，文件/文件夹大小无限制）
	 * var fileTree = Files.wholeTree("/path/to/a");
	 * // 打印树状图
	 * System.out.println(fileTree);
	 * }</pre>
	 */
	public static FileTree wholeTree(String path) {
		return tree(path, Integer.MAX_VALUE, 0);
	}

	/**
	 * 完整树状图（层数、文件/文件夹大小无限制）
	 * @param dir 搜索文件夹
	 * @return 文件树对象
	 * @see #wholeTree(String)
	 */
	public static FileTree wholeTree(File dir) {
		return wholeTree(dir.getAbsolutePath());
	}

	/**
	 * 文件树
	 */
	@EqualsAndHashCode
	public static class FileTree {
		/**
		 * 当前文件/文件夹
		 */
		@Getter
		private final File currFile;

		/**
		 * 当前文件/文件夹长度（如果是文件夹则包含子项长度）
		 */
		@Getter
		private long length;

		/**
		 * 子文件树列表
		 */
		@Getter
		private final TreeSet<FileTree> subFileTrees = new TreeSet<>(DEFAULT_COMPARATOR);

		/**
		 * 子文件树列表默认的比较器
		 */
		private static final Comparator<FileTree> DEFAULT_COMPARATOR =
				Comparator.<FileTree>comparingLong(fileTree -> fileTree.length).reversed();

		private FileTree(File currFile) {
			this.currFile = currFile;
		}

		/**
		 * 获得文件树实例
		 * @param dirFile 搜索文件夹的File对象
		 * @param depth 搜索深度
		 * @param minBytes 结果中的文件/文件夹的最小字节长度
		 * @return 实例对象
		 */
		public static FileTree getInstance(File dirFile, int depth, long minBytes) {
			return new Object() {
				FileTree createLoop(File dirFile, long minBytes, int depth, int currDepth) {
					// 当前文件树对象
					var fileTree = new FileTree(dirFile);

					// 当前文件夹大小
					long length = 0;

					var list = dirFile.listFiles();
					if (list == null) {
						// 打印错误信息
						System.err.println("展开文件夹失败：" + dirFile.getAbsolutePath());
						return fileTree;
					}

					// 当前文件深度增加
					currDepth++;

					for (File f : list) {
						if (f.isDirectory()) {
							// 递归生成该文件夹的文件树，称为子文件树
							var subFile = createLoop(f, minBytes, depth, currDepth);

							// 如果该文件夹大小符合最小字节数并且符合搜索深度要求就放进结果中
							if (currDepth <= depth && subFile.length >= minBytes) {
								// 将该文件夹（子文件树）放入当前文件树的子文件树列表中
								fileTree.subFileTrees.add(subFile);
							}

							// 将子文件夹大小加入父文件夹
							length += subFile.length;
						} else {
							length += f.length();
							// 如果该文件大小符合最小字节数并且符合搜索深度要求就放进结果中
							if (currDepth <= depth && f.length() >= minBytes) {
								// 新建一个子文件树，包含该文件
								var subFile = new FileTree(f);
								subFile.length = f.length();
								// 将这个子文件树放进当前文件树的子文件树列表中
								fileTree.subFileTrees.add(subFile);
							}
						}
					}

					fileTree.length = length;
					return fileTree;
				}
			}.createLoop(dirFile, minBytes, depth, 0);
		}

		@Override
		public String toString() {
			return new Object() {
				String toString(FileTree fileTree, int currDepth) {
					// 储存结果的容器
					var sb = new StringBuilder();

					sb.append(" ".repeat(4 * currDepth))
							.append("├─").append(fileTree.currFile.getName())
							.append(" ").append(lengthFormatter(fileTree.length)).append("\n");

					currDepth++;

					for (var subTree : fileTree.subFileTrees) {
						sb.append(toString(subTree, currDepth));
					}
					return sb.toString();
				}
			}.toString(this, 0);
		}
	}
	// endregion
}
