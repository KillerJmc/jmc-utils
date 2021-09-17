package com.jmc.io;

import com.jmc.lang.extend.Objs;
import com.jmc.lang.extend.Strs;
import com.jmc.lang.extend.Tries;
import lombok.Getter;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;
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
 *   2021.8.17     优化Logger，在关闭情况下减少资源损耗（降低了20%耗时）
 * </pre>
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Files
{
	/**
	 * 大文件指定界限
	 */
	private static final int LARGE_FILE_SIZE = 1024 * 1024 * 500;

	/**
	 * 最多正在操作文件数
	 */
	private static final int MAX_OPERATING_AMOUNT = 500;

	/**
	 * 全局日志打印
	 */
	private static final Logger LOGGER = Logger.getLogger("Files");

	/**
	 * 是否开启日志打印
	 */
	private static boolean enableLog = false;

	/**
	 * 工具类不能被实例化
	 */
	private Files() {}

	/**
	 * 设置是否打印日志
	 * @param enable 是否打印日志
	 */
	public static void enableLog(boolean enable) {
		enableLog = enable;
	}

	/**
	 * 打印日志（懒加载提升性能）
	 * @param msg 日志内容
	 */
	public static void log(Supplier<String> msg) {
		if (enableLog) {
			LOGGER.info(msg.get());
		}
	}

	/**
	 * 复制文件或文件夹
	 * @param srcPath 源路径
	 * @param desPath 目标路径
	 */
	public static void copy(String srcPath, String desPath) {
        // 路径不能为空
        Objs.throwsIfNullOrEmpty("源路径和目标路径不能为空！",
				srcPath, desPath);

        // 创建源文件
        File src = new File(srcPath);
        
        // 检查路径是否存在
        if (!src.exists()) {
        	throw new RuntimeException("源文件不存在，复制失败");
		}

        // 创建目标文件
        File des = new File(desPath + "/" + src.getName());

        // 创建目标路径
		mkdirs(src.isDirectory() ? des : des.getParentFile());

        // 线程池
		var pool = Executors.newFixedThreadPool(MAX_OPERATING_AMOUNT);
        
        // 记录开始时间
        long startTime = System.currentTimeMillis();       
              
		log(() -> "正在复制 %s 这个%s".formatted(src.getName(), src.isFile() ? "文件" : "文件夹"));

        // 如果是文件
        if (src.isFile()) {
        	// 如果是小文件
            if (src.length() < LARGE_FILE_SIZE) {
            	// 小文件复制通道
            	pool.execute(new SmallCopyThread(src, des));
			} else {
            	// 大文件复制通道
				pool.execute(new LargeCopyThread(src, des));
			}
        } else {
            // 递归复制文件
            copyLoop(src, src.getAbsolutePath().length(), des.getAbsolutePath(), pool);
        }

		pool.shutdown();
        
        // 等待执行完成
		Tries.tryThis(() -> { while (!pool.awaitTermination(1, TimeUnit.HOURS)); });

		// 统计时间
		long endTime = System.currentTimeMillis();
		log(() -> "耗时" + (double)((endTime - startTime) / 1000) + "秒，已完成");
    }

	/**
	 * 复制文件或文件夹
	 * @param src 源文件
	 * @param destPath 目标路径
	 */
	public static void copy(File src, String destPath) {
		copy(src.getAbsolutePath(), destPath);
	}

	/**
	 * 递归复制文件
	 * @param f 复制的文件
	 * @param srcPathLength 源路径长度
	 * @param destRootPath 目标根路径
	 * @param pool 线程池
	 */
	private static void copyLoop(File f, int srcPathLength, String destRootPath, ExecutorService pool) {
        File[] fs = f.listFiles();
        if (fs == null) {
        	throw new RuntimeException("展开文件夹失败");
		}

        for (File src : fs) {
            // 创建目标文件/文件夹
            File des = new File(destRootPath + src.getAbsolutePath().substring(srcPathLength));
            // 若现在循环内的这个文件是个目录
            if (src.isDirectory()) {
                // 创建这个目录
                mkdirs(des);
                // 递归复制
                copyLoop(src, srcPathLength, destRootPath, pool);
            } else {
                // 如果该文件为大文件
                if (src.length() > LARGE_FILE_SIZE) {
                    // 大文件通道
                	pool.execute(new LargeCopyThread(src, des));
                } else {
                    // 小文件通道
					pool.execute(new SmallCopyThread(src, des));
                }
            }
        }
    }

	/**
	 * 移动文件或文件夹
	 * @param srcPath 源路径
	 * @param desPath 目标路径
	 */
	public static void move(String srcPath, String desPath) {
		Objs.throwsIfNullOrEmpty("根路径和目标路径不能为空",
				srcPath, desPath);

        // 创建源文件
        File src = new File(srcPath);

        // 判断源文件是否存在
        if (!src.exists()) {
            throw new RuntimeException("源文件不存在");
        }

        // 目标文件
        File des = new File(desPath + "/" + src.getName());

        // 创建目标父目录
        File parent = des.getParentFile();

        // 创建目录
        mkdirs(parent);

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
        log(() -> "成功将 " + src.getName() + " 移动到 " + parent.getName() + " 文件夹!");
    }

	/**
	 * 移动文件或文件夹
	 * @param src 文件或文件夹的File对象
	 * @param desPath 目标路径
	 */
	public static void move(File src, String desPath) {
		move(src.getAbsolutePath(), desPath);
	}

	/**
	 * 重命名文件或文件夹
	 * @param filePath 路径
	 * @param newName 新名称
	 */
    public static void rename(String filePath, String newName) {
    	Objs.throwsIfNullOrEmpty("文件路径和新名称不能为空",
				filePath, newName);

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
	 */
	public static void rename(File src, String newName) {
		rename(src.getAbsolutePath(), newName);
	}

	/**
	 * 删除文件或文件夹
	 * @param path 路径
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
        deleteLoop(f);

        // 统计时间
        long endTime = System.currentTimeMillis();

        log(() -> "耗时" + (double)((endTime - startTime) / 1000) + "秒，已完成");
    }

	/**
	 * 删除文件或文件夹
	 * @param src 源文件的File对象
	 */
	public static void delete(File src) {
		delete(src.getAbsolutePath());
	}

	/**
	 * 删除多个文件或文件夹
	 * @param fs 文件或文件夹
	 */
	public static void delete(File... fs) {
		for (File f : fs) {
			delete(f);
		}
	}

	/**
	 * 删除多个文件或文件夹
	 * @param list 文件列表
	 */
    public static void delete(List<File> list) {
		for (File f : list) {
			delete(f);
		}
    }

	/**
	 * 递归删除文件或文件夹
	 * @param f 文件
	 */
	private static void deleteLoop(File f) {
        // 如果不能直接删除
        if (!f.delete()){
            // 递归删除
            File[] fs = f.listFiles();

            if (fs == null) {
            	throw new RuntimeException("展开文件夹失败！");
			}

            for (File del : fs) {
                deleteLoop(del);
            }

            // 删除之前的非空文件夹
            if (!f.delete()) {
            	throw new RuntimeException("删除失败！");
			}
        } else {
        	log(() -> "正在删除：" + f.getAbsolutePath());
		}
    }

	/**
	 * 压缩文件或文件夹
	 * @param srcPath 源路径
	 * @param zipPath zip路径
	 * @param storeMode 是否用储存式压缩
	 */
    public static void zip(String srcPath, String zipPath, boolean storeMode) {
		Objs.throwsIfNullOrEmpty("源路径和zip路径不能为空",
				srcPath, zipPath);

        // 创建源文件
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
        zipLoop(out, src, src.getName(), storeMode);

        // 关闭zip输出流
		Tries.tryThis(out::close);

		long endTime = System.currentTimeMillis();
		log(() -> "耗时" + (double) ((endTime - startTime) / 1000) + "秒，已完成");
    }

	/**
	 * 压缩文件或文件夹
	 * @param src 源文件
	 * @param zipPath zip路径
	 * @param storeMode 是否用储存式压缩
	 */
	public static void zip(File src, String zipPath, boolean storeMode) {
		zip(src.getAbsolutePath(), zipPath, storeMode);
	}

	/**
	 * 在本目录下创建压缩文件
	 * @param srcPath 源路径
	 * @param storeMode 是否用储存式压缩
	 */
	public static void zip(String srcPath, boolean storeMode) {
        // 声明原路径
        File src = new File(srcPath);
        // zip生成路径为源路径
        String zipPath = src.getParent() + "/" + src.getName() + ".zip";
        // 开始创建
        zip(srcPath, zipPath, storeMode);
    }

	/**
	 * 在本目录下创建压缩文件
	 * @param src 源文件
	 * @param storeMode 是否用储存式压缩
	 */
	public static void zip(File src, boolean storeMode) {
		zip(src.getAbsolutePath(), storeMode);
	}

	/**
	 * 递归创建zip
	 * @param out zip输出流
	 * @param f 源文件
	 * @param root 根路径
	 * @param storeMode 是否用储存式压缩
	 */
    private static void zipLoop(ZipOutputStream out, File f, String root, boolean storeMode) {
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
                    zipLoop(out, src, filePath, storeMode);
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

	/**
	 * 解压文件
	 * @param zipPath zip路径
	 * @param desPath 目标路径
	 */
	public static void unzip(String zipPath, String desPath) {
		Objs.throwsIfNullOrEmpty("zip路径和目标路径不能为空",
				zipPath, desPath);

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
                pool.execute(new ZipOutputThread(zip, des, entry));
            }
        }
        pool.shutdown();

		// 等待执行完成
		Tries.tryThis(() -> { while (!pool.awaitTermination(1, TimeUnit.HOURS)); });

        //关闭流
		Tries.tryThis(zip::close);

		long endTime = System.currentTimeMillis();
		log(() -> "耗时" + (double) ((endTime - startTime) / 1000) + "秒，已完成");
    }

	/**
	 * 解压文件
	 * @param zip zip文件
	 * @param desPath 目标路径
	 */
	public static void unzip(File zip, String desPath) {
		unzip(zip.getAbsolutePath(), desPath);
	}

	/**
	 * 在本目录下解压文件
	 * @param zipPath zip文件路径
	 */
	public static void unzip(String zipPath) {
        // 声明原zip路径
        File zip = new File(zipPath);
        // zip父目录为解压目录
        String desPath = zip.getParent();
        // zip开始解压
        unzip(zipPath, desPath);
    }

	/**
	 * 在本目录下解压文件
	 * @param zip zip文件
	 */
	public static void unzip(File zip) {
		unzip(zip.getAbsolutePath());
	}

	/**
	 * 默认系统编码读取文件到字符串
	 * @param path 源文件路径
	 * @return 结果字符串
	 */
	public static String read(String path) {
		return read(path, Charset.defaultCharset());
	}

	/**
	 * 默认系统编码读取文件到字符串
	 * @param src 源文件
	 * @return 结果字符串
	 */
	public static String read(File src) {
		return read(src, Charset.defaultCharset());
	}

	/**
	 * 读取文件到字符串
	 * @param src 源文件
	 * @param cs 文件编码
	 * @return 结果字符串
	 */
	public static String read(File src, Charset cs) {
		return new String(readToBytes(src), cs);
	}

	/**
	 * 读取文件到字符串
	 * @param path 源文件路径
	 * @param cs 文件编码
	 * @return 结果字符串
	 */
	public static String read(String path, Charset cs) {
		return new String(readToBytes(path), cs);
	}

	/**
	 * 读取文件到byte数组
	 * @param path 源路径
	 * @return 结果数组
	 */
	public static byte[] readToBytes(String path) {
		return readToBytes(new File(path));
	}

	/**
	 * 读取文件到byte数组
	 * @param src 源文件
	 * @return 结果数组
	 */
	public static byte[] readToBytes(File src) {
		// 若文件不存在
		if (!src.exists()) {
			throw new RuntimeException("文件不存在");
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
	 * 用非追加模式和系统默认编码输出字符串到文件
	 * @param s 字符串
	 * @param desPath 目标文件路径
	 */
	public static void out(String s, String desPath) {
		out(s, desPath, false);
	}

	/**
	 * 用非追加模式和系统默认编码输出字符串到文件
	 * @param s 字符串
	 * @param des 目标文件
	 */
	public static void out(String s, File des) {
		out(s, des, false);
	}

	/**
	 * 用系统默认编码输出字符串到文件
	 * @param s 字符串
	 * @param desPath 目标文件路径
	 * @param appendMode 是否用追加模式
	 */
	public static void out(String s, String desPath, boolean appendMode) {
		out(s, desPath, Charset.defaultCharset(), appendMode);
	}

	/**
	 * 用系统默认编码输出字符串到文件
	 * @param s 字符串
	 * @param des 目标文件
	 * @param appendMode 是否用追加模式
	 */
	public static void out(String s, File des, boolean appendMode) {
		out(s, des, Charset.defaultCharset(), appendMode);
	}

	/**
	 * 输出字符串到文件
	 * @param s 字符串
	 * @param desPath 目标文件路径
	 * @param desCharset 目标文件编码名称
	 * @param appendMode 是否用追加模式
	 */
	public static void out(String s, String desPath, Charset desCharset, boolean appendMode) {
		out(s.getBytes(desCharset), desPath, appendMode);
	}

	/**
	 * 输出字符串到文件
	 * @param s 字符串
	 * @param des 目标文件
	 * @param desCharset 目标文件编码名称
	 * @param appendMode 是否用追加模式
	 */
	public static void out(String s, File des, Charset desCharset, boolean appendMode) {
		out(s.getBytes(desCharset), des, appendMode);
	}

	/**
	 * 用非追加模式把byte数组输出到文件
	 * @param bs byte数组
	 * @param desPath 目标文件路径
	 */
	public static void out(byte[] bs, String desPath) {
		out(bs, desPath, false);
	}

	/**
	 * 用非追加模式把byte数组输出到文件
	 * @param bs byte数组
	 * @param des 目标文件
	 */
	public static void out(byte[] bs, File des) {
		out(bs, des, false);
	}

	/**
	 * 把byte数组输出到文件
	 * @param bs byte数组
	 * @param desPath 目标文件路径
	 * @param appendMode 是否为追加模式
	 */
	public static void out(byte[] bs, String desPath, boolean appendMode) {
		out(bs, new File(desPath), appendMode);
	}

	/**
	 * 把byte数组输出到文件
	 * @param bs byte数组
	 * @param des 目标文件
	 * @param appendMode 是否为追加模式
	 */
	public static void out(byte[] bs, File des, boolean appendMode) {
		Objs.throwsIfNullOrEmpty("byte数组和目标文件不能为空",
				bs, des);

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
	 * 输入流输出到文件
	 * @param in 输入流
	 * @param filePath 文件路径
	 * @param appendMode 是否用追加模式
	 */
	public static void out(InputStream in, String filePath, boolean appendMode) {
		out(in, new File(filePath), appendMode);
	}

	/**
	 * 输入流输出到文件
	 * @param in 输入流
	 * @param f 文件
	 * @param appendMode 是否用追加模式
	 */
	public static void out(InputStream in, File f, boolean appendMode) {
		Objs.throwsIfNullOrEmpty("输入流和文件参数不能为空",
				in, f);

		mkdirs(f.getParentFile());

		try (in; var out = new FileOutputStream(f, appendMode)) {
			in.transferTo(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 搜索结果类
	 * @param files 结果文件列表
	 * @param dirs 结果文件夹列表
	 */
	public static record FindResult(List<File> files, List<File> dirs) {}

	/**
	 * 文件信息
	 * @param path 路径
	 * @return 包含文件信息的字符串
	 */
	public static String fileInfo(String path) {
		File src = new File(path);
		if (!src.exists()) {
			throw new RuntimeException("路径不存在！");
		}

		// 文件夹临时列表
		List<File> temp = new ArrayList<>();

		// 文件总大小 文件个数 文件夹个数
		long length = 0, files = 0, dirs = 0;

		long startTime = System.currentTimeMillis();

		if (src.isDirectory()) {
			temp.add(src);
			while (!temp.isEmpty()){
				var list = temp.remove(0).listFiles();
				if (list == null) return null;

				for (File f : list) {
					if (f.isDirectory()){
						temp.add(f);
						dirs++;
					} else {
						length += f.length();
						files++;
					}
				}
			}
		} else {
			files = 1;
			length = src.length();
		}

		long endTime = System.currentTimeMillis();

		return String.format("""
      
		正在统计：%s
		
		共%d个文件
		%d个文件夹
		大小为%s (%s个字节)
		本次统计耗时%d秒
		""", src.getName(), files, dirs, lengthFormatter(length), new DecimalFormat().format(length), (endTime - startTime) / 1000);
	}

	/**
	 * 文件长度转换
	 * @param length 文件大小
	 * @return 格式化后的文件长度
	 */
	public static String lengthFormatter(long length) {
		long B = 1, KB = B * 1024, MB = KB * 1024,
				GB = MB * 1024, TB = GB * 1024,
				PB = TB * 1024, EB = PB * 1024;

		return length < KB ? length + "B" :
				length < MB ? "%.2fKB".formatted((double) length / KB) :
						length < GB ? "%.2fMB".formatted((double) length / MB) :
								length < TB ? "%.2fGB".formatted((double) length / GB) :
										length < PB ? "%.2fTB".formatted((double) length / TB) :
												length < EB ? "%.2fPB".formatted((double) length / PB) :
														"%.2fEB".formatted((double) length / EB);
	}

	/**
	 * 创建多级目录
	 * @param path 多级目录路径
	 */
	public static void mkdirs(String path) {
		mkdirs(new File(path));
	}

	/**
	 * 创建多级目录
	 * @param f 多级目录的File对象
	 */
	public static void mkdirs(File f) {
		Objs.throwsIfNullOrEmpty("文件对象不能为空！", f);

		if (f.exists()) return;

		if (!f.mkdirs()) {
			throw new RuntimeException("创建多级目录失败！");
		}
	}

	/**
	 * 设置文件编码
	 * @param src 源文件
	 * @param oldCharset 旧编码
	 * @param newCharset 新编码
	 */
	public static void setEncoding(File src, Charset oldCharset, Charset newCharset) {
		byte[] srcBytes = readToBytes(src);
		byte[] bs = new String(srcBytes, oldCharset)
				.getBytes(newCharset);
		out(bs, src);
	}

	/**
	 * 设置文件编码
	 * @param path 源文件路径
	 * @param oldCharset 旧编码名称
	 * @param newCharset 新编码名称
	 */
	public static void setEncoding(String path, Charset oldCharset, Charset newCharset) {
		File src = new File(path);
		setEncoding(src, oldCharset, newCharset);
	}

	/**
	 * 按时间重命名
	 * @param dirFile 搜索文件夹的File对象
	 * @param suffix 后缀（含小数点）
	 */
	public static void renameByTime(File dirFile, String suffix) {
		Objs.throwsIfNullOrEmpty(dirFile, suffix);

		File[] fs = dirFile.listFiles(File::isFile);
		if (fs == null) return;

		Arrays.sort(fs, Comparator.comparingLong(File::lastModified));

		for (int i = 0; i < fs.length; i++) rename(fs[i], (i + 1) + suffix);
	}

	/**
	 * 按时间重命名
	 * @param dirPath 搜索文件夹路径
	 * @param suffix 后缀（含小数点）
	 */
	public static void renameByTime(String dirPath, String suffix) {
		renameByTime(new File(dirPath), suffix);
	}

	/**
	 * 搜索路径下符合要求的所有文件和文件夹
	 * @param dirPath 搜索文件夹路径
	 * @param contains 文件或文件夹名称中包含的内容
	 * @return 含结果文件和文件夹的集合
	 */
	public static FindResult findAll(String dirPath, String... contains) {
		FileFilter filter = f -> Strs.orContains(f.getName() ,
				contains.length == 0 ? new String[] {""} : contains);
		return Files.findAll(dirPath, filter);
	}

	/**
	 * 搜索路径下符合要求的所有文件和文件夹
	 * @param dirPath 搜索文件夹路径
	 * @param filter 文件过滤器
	 * @return 含结果文件和文件夹的集合
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

		findLoop(src, filter, fileList, dirList);

		return new FindResult(fileList, dirList);
	}

	/**
	 * 搜索路径下符合要求的所有文件
	 * @param dirPath 搜索文件夹路径
	 * @param filter 文件过滤器
	 * @return 含结果文件的列表
	 */
	public static List<File> findFiles(String dirPath, FileFilter filter) {
		return findAll(dirPath, filter).files();
	}

	/**
	 * 搜索路径下符合要求的所有文件
	 * @param dirPath 搜索文件夹路径
	 * @param contains 文件名称中包含的内容
	 * @return 含结果文件的列表
	 */
	public static List<File> findFiles(String dirPath, String... contains) {
		return findAll(dirPath, contains).files();
	}

	/**
	 * 搜索路径下符合要求的所有文件夹
	 * @param dirPath 搜索文件夹路径
	 * @param filter 文件过滤器
	 * @return 含结果文件夹的列表
	 */
	public static List<File> findDirs(String dirPath, FileFilter filter) {
		return findAll(dirPath, filter).dirs();
	}

	/**
	 * 搜索路径下符合要求的所有文件夹
	 * @param dirPath 搜索文件夹路径
	 * @param contains 文件夹名称中包含的内容
	 * @return 含结果文件的列表
	 */
	public static List<File> findDirs(String dirPath, String... contains) {
		return findAll(dirPath, contains).dirs();
	}

	/**
	 * 递归搜索文件
	 * @param src 源文件
	 * @param filter 文件过滤器
	 * @param fileList 结果文件列表
	 * @param dirList 结果文件夹列表
	 */
	private static void findLoop(File src, FileFilter filter, List<File> fileList, List<File> dirList) {
		File[] fs = src.listFiles();
		if (fs == null) return;

		for (File f : fs) {
			if (f.isFile()) {
				if (filter.accept(f)) fileList.add(f);
			} else {
				if (filter.accept(f)) dirList.add(f);
				findLoop(f, filter, fileList, dirList);
			}
		}
	}

	/**
	 * 搜索单个文件
	 * @param dirPath 搜索文件夹路径
	 * @param content 文件名称包含内容
	 * @return 搜索结果
	 */
	public static File findAny(String dirPath, String content) {
		File src = new File(dirPath);
		if (!src.exists()) {
			throw new RuntimeException("路径不存在!");
		}

		if (src.isFile()) {
			throw new RuntimeException("搜索父目录必须为文件夹！");
		}

		// 文件夹临时列表
		var temp = new ArrayList<File>() {{ add(src); }};

		while (!temp.isEmpty()) {
			var list = temp.remove(0).listFiles();
			if (list == null) return null;

			for (File f : list) {
				if (f.isDirectory()){
					temp.add(f);
				} else {
					if (f.getName().contains(content)) return f;
				}
			}
		}

		return null;
	}

	/**
	 * 搜索单个文件
	 * @param dirFile 搜索文件夹的File对象
	 * @param content 文件名称包含内容
	 * @return 搜索结果
	 */
	public static File findAny(File dirFile, String content) {
		return findAny(dirFile.getAbsolutePath(), content);
	}

	/**
	 * 搜索文件和文件夹并保存结果到字符串
	 * @param dirPath 搜索文件夹路径
	 * @param content 文件或文件夹包含内容
	 * @return 字符串形式的搜索结果
	 */
	public static String findInfo(String dirPath, String content) {
		Objs.throwsIfNullOrEmpty(content);

		long startTime = System.currentTimeMillis();
		var map = Files.findAll(dirPath, content);
		var sb = new StringBuilder();

		var dirList = map.dirs();
		var fileList = map.files();

		sb.append("文件夹：\n");
		for (File d : dirList) sb.append(d.getAbsolutePath()).append("\n");

		sb.append("\n文件：\n");
		for (File f : fileList) sb.append(f.getAbsolutePath()).append("\n");

		long endTime = System.currentTimeMillis();
		sb.append("\n共搜索到\n").append(dirList.size()).append("个文件夹\n")
				.append(fileList.size()).append("个文件")
				.append("\n本次搜索耗时").append((int) (endTime - startTime) / 1000).append("秒\n");

		return sb.toString();
	}

	/**
	 * 搜索文件和文件夹并保存结果到字符串
	 * @param dirFile 搜索文件夹的File对象
	 * @param content 文件或文件夹名称包含内容
	 * @return 字符串形式的搜索结果
	 */
	public static String findInfo(File dirFile, String content) {
		return findInfo(dirFile.getAbsolutePath(), content);
	}

	/**
	 * 寻找并复制符合要求的文件
	 * @param dirPath 搜索文件夹路径
	 * @param desPath 目标路径
	 * @param orContains 文件名称内包含内容
	 */
	public static void findCopies(String dirPath, String desPath, String... orContains) {
		findDo(fileList -> { for (File f : fileList) copy(f, desPath); },
				dirPath, orContains);
	}

	/**
	 * 寻找并复制符合要求的文件
	 * @param dirFile 搜索文件夹的File对象
	 * @param desPath 目标路径
	 * @param orContains 文件名称内包含内容
	 */
	public static void findCopies(File dirFile, String desPath, String... orContains) {
		findCopies(dirFile.getAbsolutePath(), desPath, orContains);
	}

	/**
	 * 寻找并移动符合要求的文件
	 * @param dirPath 搜索文件夹路径
	 * @param desPath 目标路径
	 * @param orContains 文件名称内包含内容
	 */
	public static void findMoves(String dirPath, final String desPath, String... orContains) {
		findDo(fileList -> { for (File f : fileList) move(f, desPath); },
				dirPath, orContains);
	}

	/**
	 * 寻找并移动符合要求的文件
	 * @param dirFile 搜索文件夹的File对象
	 * @param desPath 目标路径
	 * @param orContains 文件名称内包含内容
	 */
	public static void findMoves(File dirFile, String desPath, String... orContains) {
		findMoves(dirFile.getAbsolutePath(), desPath, orContains);
	}

	/**
	 * 寻找并重命名符合要求的文件
	 * @param dirPath 搜索文件夹路径
	 * @param newChar 新名称
	 * @param oldChars 旧文件的名称
	 */
	public static void findRenames(String dirPath, String newChar, String... oldChars) {
		findDo(fileList -> { for (File f : fileList) rename(f, Strs.orReplace(f.getName(), newChar, oldChars)); },
				dirPath, oldChars);
	}

	/**
	 * 寻找并重命名符合要求的文件
	 * @param dirFile 搜索文件夹的File对象
	 * @param newChar 新名称
	 * @param oldChars 旧文件的名称
	 */
	public static void findRenames(File dirFile, String newChar, String... oldChars) {
		findRenames(dirFile.getAbsolutePath(), newChar, oldChars);
	}

	/**
	 * 寻找并删除符合要求的文件
	 * @param dirPath 搜索文件夹路径
	 * @param orContains 文件名称内包含内容
	 */
	public static void findDels(String dirPath, String... orContains) {
		findDo((fileList) -> {
			for (File f : fileList) {
				if (!f.delete()) {
					throw new RuntimeException("删除失败！");
				}
			}
		}, dirPath, orContains);
	}

	/**
	 * 寻找并删除符合要求的文件
	 * @param dirFile 搜索文件夹的File对象
	 * @param orContains 文件名称内包含内容
	 */
	public static void findDels(File dirFile, String... orContains) {
		findDels(dirFile.getAbsolutePath(), orContains);
	}

	/**
	 * 寻找并做模板
	 * @param c 执行模块（传入一个搜索文件列表）
	 * @param dirPath 搜索文件夹路径
	 * @param orContains 文件或文件夹名称包含内容
	 */
	private static void findDo(Consumer<List<File>> c, String dirPath, String... orContains) {
		if (orContains.length == 0) {
			throw new RuntimeException("搜索字符不能为空！");
		}

		c.accept(findFiles(dirPath, orContains));
	}

	/**
	 * 普通树状图（搜索深度5层，文件夹最小大小为50MB）
	 * @param dirPath 搜索文件夹路径
	 * @return 文件树对象
	 */
	public static FileTree normalTree(String dirPath) {
		File dirFile = new File(dirPath);
		return normalTree(dirFile);
	}

	/**
	 * 普通树状图（搜索深度5层，文件夹最小大小为50MB）
	 * @param dirFile 搜索文件夹的File对象
	 * @return 文件树对象
	 */
	public static FileTree normalTree(File dirFile) {
		return tree(dirFile, 5, 50);
	}

	/**
	 * 单层树状图（文件夹最小大小为50MB）
	 * @param dirPath 搜索文件夹路径
	 * @return 文件树对象
	 */
	public static FileTree singleTree(String dirPath) {
		File dirFile = new File(dirPath);
		return singleTree(dirFile);
	}

	/**
	 * 单层树状图（文件夹最小大小为50MB）
	 * @param dirFile 搜索文件夹的File对象
	 * @return 文件树对象
	 */
	public static FileTree singleTree(File dirFile) {
		return tree(dirFile, 1, 50);
	}

	/**
	 * 完整树状图（层数，文件夹大小无限制）
	 * @param dirPath 搜索文件夹路径
	 * @return 文件树对象
	 */
	public static FileTree wholeTree(String dirPath) {
		File dirFile = new File(dirPath);
		return wholeTree(dirFile);
	}

	/**
	 * 完整树状图（层数，文件夹大小无限制）
	 * @param dirFile 搜索文件夹的File对象
	 * @return 文件树对象
	 */
	public static FileTree wholeTree(File dirFile) {
		return tree(dirFile, Integer.MAX_VALUE, 0);
	}

	/**
	 * 树状图统计文件夹
	 * @param dirPath 搜索文件夹路径
	 * @param depth 搜索深度
	 * @param MIN_MB_SIZE 结果中的文件夹最小多少MB
	 * @return 文件树对象
	 */
	public static FileTree tree(String dirPath, int depth, double MIN_MB_SIZE) {
		File dirFile = new File(dirPath);
		return tree(dirFile, depth, MIN_MB_SIZE);
	}

	/**
	 * 树状图统计文件夹
	 * @param dirFile 统计文件夹的File对象
	 * @param depth 搜索深度
	 * @param MIN_MB_SIZE 结果中的文件夹最小多少MB
	 * @return 文件树对象
	 */
	public static FileTree tree(File dirFile, int depth, double MIN_MB_SIZE) {
		if (!dirFile.exists()) {
			throw new RuntimeException("文件夹不存在");
		}

		if (dirFile.isFile()) {
			throw new RuntimeException("统计的对象不能为文件！");
		}

		long MIN_LENGTH = (long) (MIN_MB_SIZE * 1024 * 1024);

		return FileTree.getInstance(dirFile, depth, MIN_LENGTH);
	}


	/**
	 * 文件树
	 */
	public static class FileTree {
		/**
		 * 当前文件/文件夹
		 */
		@Getter
		private File currFile;

		/**
		 * 当前文件/文件夹长度（如果是文件夹则包含子项长度）
		 */
		@Getter private long length;

		/**
		 * 子文件树列表
		 */
		@Getter private final TreeSet<FileTree> subFileTrees = new TreeSet<>(DEFAULT_COMPARATOR);

		/**
		 * 子文件树列表默认的比较器
		 */
		private static final Comparator<FileTree> DEFAULT_COMPARATOR = Comparator.comparingLong(FileTree::getLength).reversed();
		private FileTree() {
			// 禁止外部调用构造器
		}

		/**
		 * 获得文件树实例
		 * @param dirFile 搜索文件夹的File对象
		 * @param depth 搜索深度
		 * @param MIN_LENGTH  结果中的文件和文件夹最小长度
		 * @return 实例对象
		 */
		public static FileTree getInstance(File dirFile, int depth, long MIN_LENGTH) {
			return createLoop(dirFile, MIN_LENGTH, depth, 0);
		}

		/**
		 * 递归创建实例
		 * @param dirFile 搜索文件夹的File对象
		 * @param MIN_LENGTH 结果中的文件和文件夹的最小长度
		 * @param depth 搜索深度
		 * @param currDepth 当前搜索深度
		 * @return 实例对象
		 */
		private static FileTree createLoop(File dirFile, long MIN_LENGTH, int depth, int currDepth) {
			// 当前文件树对象
			var fileTree = new FileTree();

			// 当前文件夹大小
			long length = 0;

			var list = dirFile.listFiles();
			if (list == null) {
				throw new RuntimeException("展开文件夹失败！");
			}

			currDepth++;

			for (File f : list) {
				if (f.isDirectory()) {
					var subFile = createLoop(f, MIN_LENGTH, depth, currDepth);
					if (currDepth <= depth && subFile.length >= MIN_LENGTH) {
						fileTree.subFileTrees.add(subFile);
					}

					// 将子文件夹大小加入父文件夹
					length += subFile.length;
				} else {
					length += f.length();
					if (currDepth <= depth && f.length() >= MIN_LENGTH) {
						var subFile = new FileTree();
						subFile.currFile = f;
						subFile.length = f.length();
						fileTree.subFileTrees.add(subFile);
					}
				}
			}

			fileTree.currFile = dirFile;
			fileTree.length = length;

			return fileTree;
		}

		@Override
		public String toString() {
			return toString(this, 0);
		}

		private String toString(FileTree fileTree, int currDepth) {
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
	}

	/**
	 * 大文件复制通道 <br>
	 * 使用的是Java的零拷贝技术
	 * @param src 源文件
	 * @param des 目标文件
	 */
	private record LargeCopyThread(File src, File des) implements Runnable {
		@Override
		public void run() {
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
			try (var in = new FileInputStream(src).getChannel();
				 var out = new FileOutputStream(des).getChannel()) {

				// 申请8M的堆外内存
				var buff = ByteBuffer.allocateDirect(8 * 1024 * 1024);

				// 读取通道中数据到buff
				while (in.read(buff) != -1) {
					// 改变buff为读模式
					buff.flip();
					// 将buff写入输出通道
					out.write(buff);
					// buff的复位
					buff.clear();
				}
				// 强制将内存中剩余数据写入硬盘，保证数据完整性
				out.force(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 小文件复制通道
	 * @param src 源文件
	 * @param des 目标文件
	 */
	private record SmallCopyThread(File src, File des) implements Runnable {
		@Override
		public void run() {
			// 日志信息
			log(() -> "正在复制文件: " + src.getAbsolutePath());

			try (var in = new FileInputStream(src);
				 var out = new FileOutputStream(des)) {
				in.transferTo(out);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Zip输出通道
	 * @param zip   zip文件
	 * @param des   目标文件
	 * @param entry zip节点
	 */
	private record ZipOutputThread(ZipFile zip, File des,
								   ZipEntry entry) implements Runnable {
		@Override
		public void run() {
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
		}
	}
}
