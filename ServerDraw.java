import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class ServerDraw{	
  // Clientから入力されてきたx座標、y座標を入れておくリスト
  static ArrayList<Integer> xPoints = new ArrayList<Integer>();
  static ArrayList<Integer> yPoints = new ArrayList<Integer>();
    
  public static void main(String[] args) {
    // 最初に起動したかどうか不安であれば、print文を書くと良いです！
    System.out.println("サーバーが立ち上がりました。");
    try {
      ServerSocket serverSocket  = new ServerSocket(5000);
      
      while(true) {
        Socket socket = serverSocket.accept();
        // ここでも接続できたことを表示しておくと安心
        System.out.println("クライアントからの接続がありました");		
        
        // クライアントからの読み込み
        BufferedReader reader = new BufferedReader(
                       new InputStreamReader(socket.getInputStream()));
        String line = reader.readLine(); //1行読み込み
        System.out.println("クライアントからマウス座標=["+line+"]が入力されました");
	

       // カンマで分割して配列に格納したら、サーバー側で使いやすいですね！
        String[] x_and_y = line.split(","); 
        xPoints.add(Integer.parseInt(x_and_y[0]));
        yPoints.add(Integer.parseInt(x_and_y[1]));

	

	PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
	//writer.println("Server: 座標を受け取りました");
	writer.println(x_and_y[0]+","+x_and_y[1]);
	writer.flush();
	writer.close();
	System.out.println("sent");
	
        reader.close();
       }
     }catch(IOException e) {
           System.out.println(e);
     }
  }
}
