package cn.fomer.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;


//import org.apache.log4j.Logger;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * 2019-01-04 来自青岛移动项目（未能测试成功）
 * 
 * 
 */
public class SFtpUtil {
	//private static Logger log = Logger.getLogger(SFtpUtil.class.getName());
	private String host;
	private String username;
	private String password;
	private int port = 22;
	private ChannelSftp sftp = null;
	private Session sshSession = null;

	public SFtpUtil() {
	}

	public SFtpUtil(String host, int port, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	public SFtpUtil(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}

	public void connect() {
		try {
			JSch jsch = new JSch();
			jsch.getSession(this.username, this.host, this.port);
			this.sshSession = jsch.getSession(this.username, this.host, this.port);
//			if (log.isInfoEnabled()) {
//				log.info("Session created.");
//			}

			this.sshSession.setPassword(this.password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			this.sshSession.setConfig(sshConfig);
			this.sshSession.connect();
//			if (log.isInfoEnabled()) {
//				log.info("Session connected.");
//			}

			Channel channel = this.sshSession.openChannel("sftp");
			channel.connect();
//			if (log.isInfoEnabled()) {
//				log.info("Opening Channel.");
//			}

			this.sftp = (ChannelSftp) channel;
//			if (log.isInfoEnabled()) {
//				log.info("Connected to " + this.host + ".");
//			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

	}

	public void disconnect() {
		if (this.sftp != null && this.sftp.isConnected()) {
			this.sftp.disconnect();
//			if (log.isInfoEnabled()) {
//				log.info("sftp is closed already");
//			}
		}

		if (this.sshSession != null && this.sshSession.isConnected()) {
			this.sshSession.disconnect();
//			if (log.isInfoEnabled()) {
//				log.info("sshSession is closed already");
//			}
		}

	}

	public List<String> batchDownLoadFile(String remotePath, String localPath, String fileFormat, String fileEndFormat,
			boolean del) {
		ArrayList filenames = new ArrayList();

		try {
			try {
				Vector v = this.listFiles(remotePath);
				if (v.size() > 0) {
					System.out.println("本次处理文件个数不为零,开始下载...fileSize=" + v.size());
					Iterator it = v.iterator();

					label192 : while (true) {
						while (true) {
							String filename;
							SftpATTRS attrs;
							do {
								if (!it.hasNext()) {
									break label192;
								}

								LsEntry entry = (LsEntry) it.next();
								filename = entry.getFilename();
								attrs = entry.getAttrs();
							} while (attrs.isDir());

							boolean flag = false;
							String localFileName = localPath + filename;
							fileFormat = fileFormat == null ? "" : fileFormat.trim();
							fileEndFormat = fileEndFormat == null ? "" : fileEndFormat.trim();
							if (fileFormat.length() > 0 && fileEndFormat.length() > 0) {
								if (filename.startsWith(fileFormat) && filename.endsWith(fileEndFormat)) {
									flag = this.downloadFile(remotePath, filename, localPath, filename);
									if (flag) {
										filenames.add(localFileName);
										if (flag && del) {
											this.deleteSFTP(remotePath, filename);
										}
									}
								}
							} else if (fileFormat.length() > 0 && "".equals(fileEndFormat)) {
								if (filename.startsWith(fileFormat)) {
									flag = this.downloadFile(remotePath, filename, localPath, filename);
									if (flag) {
										filenames.add(localFileName);
										if (flag && del) {
											this.deleteSFTP(remotePath, filename);
										}
									}
								}
							} else if (fileEndFormat.length() > 0 && "".equals(fileFormat)) {
								if (filename.endsWith(fileEndFormat)) {
									flag = this.downloadFile(remotePath, filename, localPath, filename);
									if (flag) {
										filenames.add(localFileName);
										if (flag && del) {
											this.deleteSFTP(remotePath, filename);
										}
									}
								}
							} else {
								flag = this.downloadFile(remotePath, filename, localPath, filename);
								if (flag) {
									filenames.add(localFileName);
									if (flag && del) {
										this.deleteSFTP(remotePath, filename);
									}
								}
							}
						}
					}
				}

//				if (log.isInfoEnabled()) {
//					log.info("download file is success:remotePath=" + remotePath + "and localPath=" + localPath
//							+ ",file size is" + v.size());
//				}
			} catch (SftpException var17) {
				var17.printStackTrace();
			}

			return filenames;
		} finally {
			;
		}
	}

	public boolean downloadFile(String remotePath, String remoteFileName, String localPath, String localFileName) {
		FileOutputStream fieloutput = null;

		try {
			File file = new File(localPath + localFileName);
			fieloutput = new FileOutputStream(file);
			this.sftp.get(remotePath + remoteFileName, fieloutput);
//			if (log.isInfoEnabled()) {
//				log.info("===DownloadFile:" + remoteFileName + " success from sftp.");
//			}

			boolean var7 = true;
			return var7;
		} catch (FileNotFoundException var19) {
			var19.printStackTrace();
		} catch (SftpException var20) {
			var20.printStackTrace();
		} finally {
			if (null != fieloutput) {
				try {
					fieloutput.close();
				} catch (IOException var18) {
					var18.printStackTrace();
				}
			}

		}

		return false;
	}

	public boolean uploadFile(String remotePath, String remoteFileName, String localPath, String localFileName) {
		FileInputStream in = null;

		try {
			boolean createDir = this.createDir(remotePath);
			if (createDir) {
				File file = new File(localPath + localFileName);
				in = new FileInputStream(file);
				this.sftp.put(in, remoteFileName);
				boolean var8 = true;
				return var8;
			}

			boolean var7 = false;
			return var7;
		} catch (FileNotFoundException var21) {
			var21.printStackTrace();
		} catch (SftpException var22) {
			var22.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException var20) {
					var20.printStackTrace();
				}
			}

		}

		return false;
	}

	public boolean bacthUploadFile(String remotePath, String localPath, boolean del) {
		try {
			this.connect();
			File file = new File(localPath);
			File[] files = file.listFiles();

			for (int i = 0; i < files.length; ++i) {
				if (files[i].isFile() && files[i].getName().indexOf("bak") == -1
						&& this.uploadFile(remotePath, files[i].getName(), localPath, files[i].getName()) && del) {
					this.deleteFile(localPath + files[i].getName());
				}
			}

//			if (log.isInfoEnabled()) {
//				log.info("upload file is success:remotePath=" + remotePath + "and localPath=" + localPath
//						+ ",file size is " + files.length);
//			}

			boolean var12 = true;
			return var12;
		} catch (Exception var10) {
			var10.printStackTrace();
		} finally {
			this.disconnect();
		}

		return false;
	}

	public boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		} else if (!file.isFile()) {
			return false;
		} else {
			boolean rs = file.delete();
//			if (rs && log.isInfoEnabled()) {
//				log.info("delete file success from local.");
//			}

			return rs;
		}
	}

	public boolean createDir(String createpath) {
		try {
			if (this.isDirExist(createpath)) {
				this.sftp.cd(createpath);
				return true;
			} else {
				String[] pathArry = createpath.split("/");
				StringBuffer filePath = new StringBuffer("/");
				String[] var4 = pathArry;
				int var5 = pathArry.length;

				for (int var6 = 0; var6 < var5; ++var6) {
					String path = var4[var6];
					if (!path.equals("")) {
						filePath.append(path + "/");
						if (this.isDirExist(filePath.toString())) {
							this.sftp.cd(filePath.toString());
						} else {
							this.sftp.mkdir(filePath.toString());
							this.sftp.cd(filePath.toString());
						}
					}
				}

				this.sftp.cd(createpath);
				return true;
			}
		} catch (SftpException var8) {
			var8.printStackTrace();
			System.out.println(">>>>创建ftp目标文件夹异常<<<<");
			return false;
		}
	}

	public boolean isDirExist(String directory) {
		boolean isDirExistFlag = false;

		try {
			SftpATTRS sftpATTRS = this.sftp.lstat(directory);
			isDirExistFlag = true;
			return sftpATTRS.isDir();
		} catch (Exception var4) {
			if (var4.getMessage().toLowerCase().equals("no such file")) {
				isDirExistFlag = false;
			}

			return isDirExistFlag;
		}
	}

	public void deleteSFTP(String directory, String deleteFile) {
		try {
			this.sftp.rm(directory + deleteFile);
//			if (log.isInfoEnabled()) {
//				log.info("delete file success from sftp.");
//			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

	}

	public void mkdirs(String path) {
		File f = new File(path);
		String fs = f.getParent();
		f = new File(fs);
		if (!f.exists()) {
			f.mkdirs();
		}

	}

	public Vector listFiles(String directory) throws SftpException {
		return this.sftp.ls(directory);
	}

	
	public static void main(String[] args) 
	{
		//
		SFtpUtil sFtpUtil= new SFtpUtil();
		
		String path= "/data/uploadfile/other/201901/";
		String fileName= "1546570824898uyr.docx";
		String webUploadPath= "c:/uploadfile/";
		
		sFtpUtil.connect();
		boolean success= sFtpUtil.uploadFile(path,fileName,webUploadPath,fileName);
		sFtpUtil.disconnect();
		
	}
}