package net.impl.mina;

import interfac.CallString;
import interfac.InterfaceCS;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import net.Client;
import net.Client;
import net.MSG;
import net.impl.mina.coder.MyCharsetCodecFactory;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.cc.Constant;


import util.tools.MyJson;
import util.tools.Tools;

import android.graphics.drawable.Drawable.Callback;
import android.util.Log;

public abstract class ClientImpl implements Client, CallString {
    private NioSocketConnector connector;
    private ConnectFuture future;
    private IoSession session;

    public static int port = 1228;
    public static String ip = "127.0.0.1";


    public ClientImpl(String ip) {
        this.ip = ip;
    }

    @Override
    public void out(String str) {
        Tools.out("Mina." + str);
    }

    @Override
    public boolean start() {
        new Thread() {
            @Override
            public void run() {
                ip = Constant.serverIp;
                port = Constant.serverPort;
                connect();
            }
        }.start();
        return true;
    }

    @Override
    public boolean stop() {
        out(" 客户端关闭");
        try {
            session.close();
            future.cancel();
            connector.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    int ipcount = 0;
    static boolean ifOnConn = false;

    public void whileConn() {
        ifOnConn = true;

        new Thread() {
            public void run() {
                while (ifOnConn) {
                    try {
                        if (ipcount == 0) {
                            Constant.serverIp = Constant.serverIpLocal;
                        } else {
                            Constant.serverIp = Constant.serverIpNet;
                        }
                        ip = Constant.serverIp;
                        port = Constant.serverPort;

                        ipcount = 1 - ipcount;

                        // 连接服务器，知道端口、地址
                        future = connector.connect(new InetSocketAddress(ip, port));
                        out("等待服务器响应");
                        // 等待连接创建完成
                        future.awaitUninterruptibly();
//						try {
//							Thread.sleep(2000);
//						} catch (InterruptedException e1) {
//							e1.printStackTrace();
//						}
                        // 获取当前session
                        session = future.getSession();
                        out(" 链接服务器成功！ ");
                        ifOnConn = false;
                        break;
                    } catch (Exception e) {
                        out("连接异常,3s后尝试重新连接");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }.start();

    }

    public boolean connect() {
        // 创建一个socket连接
        if (connector == null) {
            connector = new NioSocketConnector();
            // 设置链接超时时间
            connector.setConnectTimeoutMillis(6000);
            // 获取过滤器链
            DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
            // 添加编码过滤器 处理乱码、编码问题
            filterChain.addLast("codec", new ProtocolCodecFilter( /*new TextLineCodecFactory(Charset.forName("UTF-8"))*/ new MyCharsetCodecFactory()));
            // 消息核心处理器
            connector.setHandler(new ClientMessageHandler(this));
        }
        whileConn();

        return true;
    }

    public void setAttribute(Object key, Object value) {
        session.setAttribute(key, value);
    }

    public void send(String message) {
        out("Send>>" + message);
        if (session != null && session.isConnected()) {
            session.write(message);
        } else {
            onReceive(MyJson.makeJson(MSG.TOAST, "连接中"));
            reconnect("");
        }

    }

    public SocketConnector getConnector() {
        return connector;
    }

    public IoSession getSession() {
        return session;
    }
///////////////////////

    public void exceptionCaught(String message) {
        out("clientimpl异常" + message);
        onReceive(MyJson.makeJson(MSG.CLOSE, "连接断开"));
        //reconnect("");
    }

    @Override
    public void onReceive(String str) {
        callback(str);
        out("Get<<" + str);
    }

    public void disConn() {
        onReceive(MyJson.makeJson(MSG.CLOSE, "连接断开"));
    }


    @Override
    public void reconnect(String string) {
        if (ifOnConn) {

        } else {
            try {
                //stop();
                session.close();
                future.cancel();
            } catch (Exception e) {
                // TODO: handle exception
            }
            Tools.log("断开重连");
            connect();
        }


    }

}