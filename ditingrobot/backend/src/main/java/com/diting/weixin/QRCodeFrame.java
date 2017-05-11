package com.diting.weixin;

import blade.kit.logging.Logger;
import blade.kit.logging.LoggerFactory;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import redis.clients.jedis.Jedis;

import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;

public class QRCodeFrame extends JFrame {

	private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeFrame.class);

	private static final long serialVersionUID = 8550014433017811556L;
	private JPanel contentPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					new QRCodeFrame("d:/a.png","");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public QRCodeFrame(final String filePath,String name) {

		Jedis jedis = new Jedis("101.201.120.206",6379);
		jedis.auth("diting");

		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone1());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		String accessKey = "uitmSQ6vcOJzagNSf_O1r3Hgc14EIWSLwoaGA8GW";
		String secretKey = "f9gzwmMZo73VtvsvhTVAShw87zFjezU2TPWK9XAw";
		String bucket = "weixin";
		//如果是Windows情况下，格式是 D:\\qiniu\\test.png
		String localFilePath = filePath;
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = null;
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket,key);
		try {
			Response response = uploadManager.put(localFilePath, key, upToken);
			//解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			LOGGER.info(Thread.currentThread().getName()+"  qiniu get name "+putRet.key);
			jedis.set(name,putRet.key);
			LOGGER.info(Thread.currentThread().getName()+"  redis get name "+(String)jedis.get(name));
//			System.out.println("qiniu get name "+putRet.key);
//			System.out.println(putRet.hash);
//			System.out.println("redis get name "+(String)jedis.get(name));
		} catch (QiniuException ex) {
			Response r = ex.response;
			System.err.println(r.toString());
			try {
				System.err.println(r.bodyString());
			} catch (QiniuException ex2) {
				//ignore
			}
		}





		setBackground(Color.WHITE);
		this.setResizable(false);
		this.setTitle("\u8BF7\u7528\u624B\u673A\u626B\u63CF\u5FAE\u4FE1\u4E8C\u7EF4\u7801");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 297, 362);
		this.contentPane = new JPanel() ;
		contentPane.setBackground(new Color(102, 153, 255));
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel qrcodePanel = new JPanel(){
			public void paintComponent(Graphics g) {
				ImageIcon icon = new ImageIcon(filePath);
				// 图片随窗体大小而变化
				g.drawImage(icon.getImage(), 0, 0, 301, 301, this);
			}
		};
		qrcodePanel.setBounds(0, 0, 295, 295);
		
		JLabel tipLable = new JLabel("扫描二维码登录微信");
		tipLable.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		tipLable.setHorizontalAlignment(SwingConstants.CENTER);
		tipLable.setBounds(0, 297, 291, 37);
		
		contentPane.add(qrcodePanel);
		contentPane.add(tipLable);
		
		this.setLocationRelativeTo(null);
//		this.setVisible(true);
	}
}
