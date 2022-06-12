package webserver;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Request implements Runnable {
	//处理网页发来的请求
    private Socket socket;
    public Request(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        InputStream input = null;
        InputStreamReader inReader = null;
        BufferedReader bReader = null;

        OutputStream out = null;
        PrintWriter pwr = null;

        Response res=new Response();
        
        try{
            input = socket.getInputStream();
            inReader = new InputStreamReader(input);
            bReader = new BufferedReader(inReader);
            out = socket.getOutputStream();
            pwr = new PrintWriter(out);
            String line = null;
            int linNum = 0;
            String reqPath = "";
            String host = "";

            while ((line=bReader.readLine())!=null){
                System.out.println(line);
                linNum++;
                if(linNum==1){
                    String[] infos = line.split(" ");
                    if(infos.length > 2) {
                        reqPath = infos[1];
                    }else {
                        throw new RuntimeException("Failed to analyse the Request: "+line);
                    }
                }else {
                    String[] infos = line.split(": ");
                    if(infos.length == 2){
                        if(infos[0].equals("Host"))
                            host = infos[1];
                    }
                }

                if(line.equals(""))
                    break;
            }
            if(!reqPath.equals("")) {
                System.out.println("the request that execute: http://" + host + reqPath);
                if(reqPath.equals("/")){
                    pwr.println("HTTP/1.1 200 OK");
                    pwr.println("Content-Type: text/html;charset=utf-8");
                    pwr.println();
                    File file = new File(GetData.resourcesPath+"/Main.html");
                    res.Responsesucc(out,file.getAbsolutePath(),"html");
                    pwr.flush();
                    System.out.println();

                }else{
                    String ext = reqPath.substring(reqPath.lastIndexOf(".")+1);
                    reqPath = reqPath.substring(1);
                    if(reqPath.contains("/")) {
                        File file = new File(GetData.resourcesPath+"/"+reqPath);
                        if(file.isFile()&& file.exists()){
                            res.Responsesucc(out,file.getAbsolutePath(),ext);
                        }else {
                            res.Responsefail(out);
                        }

                    }else {
                        File root = new File(GetData.resourcesPath);
                        if(root.isDirectory()){ 
                            File[] list = root.listFiles();
                            boolean isExist = false;
                            for(File file:list){
                                if(file.isFile() && file.getName().equals(reqPath)){
                                    isExist = true;
                                    break;
                                }
                            }
                            if(isExist){
                                res.Responsesucc(out, GetData.resourcesPath+"/"+reqPath, ext);
                            }else {
                                res.Responsefail(out);
                            }
                        }else {
                            throw new RuntimeException("Static Resources Directory not Exist! :"+GetData.resourcesPath);
                        }
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            try{
                if(input!=null)
                    input.close();
                if(inReader!=null)
                    inReader.close();
                if(bReader!=null)
                    bReader.close();
                if(pwr!=null)
                    pwr.close();
                if(out!=null)
                    out.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}

