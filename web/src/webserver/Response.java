package webserver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Response {
	//对网页的请求进行响应
	public void Responsesucc(OutputStream out,String filePath,String ext){
        PrintWriter pwr = null;
        InputStream input = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            if(ext.equals("html") || ext.equals("js") || ext.equals("css") || ext.equals("json")){
                pwr = new PrintWriter(out);
                pwr.println("HTTP/1.1 200");
                switch (ext) {
                    case "html":
                        pwr.println("Content-Type: text/html;charset=utf-8");
                        break;
                    case "js":
                        pwr.println("Content-Type: application/x-javascript");
                        break;
                    case "css":
                        pwr.println("Content-Type: text/css");
                        break;
                    case "json":
                        pwr.println("Content-Type: application/json;charset=utf-8");
                        break;
                }
                pwr.println();
                input = new FileInputStream(filePath);
                reader = new InputStreamReader(input);
                bufferedReader = new BufferedReader(reader);
                String line = null;
                while((line = bufferedReader.readLine())!=null){
                    pwr.println(line);
                    pwr.flush();
                }
            }
            else if(ext.equals("jpg") || ext.equals("png")||ext.equals("gif")){
                out.write("HTTP/1.1 200\r\n".getBytes());
                switch (ext) {
                    case "jpg":
                        out.write("Content-Type: image/jpg\r\n".getBytes());
                        break;
                    case "png":
                        out.write("Content-Type: image/png\r\n".getBytes());
                        break;
                    case "gif":
                        out.write("Content-Type: image/gif\r\n".getBytes());
                        break;
                }
                out.write("\r\n".getBytes());
                input = new FileInputStream(filePath);
                int len = -1;
                byte [] buff = new byte[1024];
                while((len = input.read(buff))!=-1){
                    out.write(buff,0,len);
                    out.flush();
                }
            }
            else{
                Responsefail(out);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(pwr!=null)
                    pwr.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void Responsefail(OutputStream out){
        PrintWriter pwr = null;
        try {
        	//如果文件不存在，输出报错
            pwr = new PrintWriter(out);
            pwr.println("HTTP/1.1 404");
            pwr.println("Content-Type: text/html;charset=utf-8");
            pwr.println();
            pwr.write("<h2>404 NOT FOUND</h2>");
            pwr.flush();
            System.out.println("Response welcome!");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(pwr!=null)
                    pwr.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
