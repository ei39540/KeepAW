import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class MouseMover  {

    public int TIME_INT = 30001;
    public int MAX_Y = 40;
    public int MAX_X = 40;
    public int DIST= 41;
    String output="WTF";
    boolean printInfo=false;
    
    Properties prop;
    
 // static variable single_instance of type Singleton 
    private static MouseMover single_instance = null; 
    
    
    public static MouseMover getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new MouseMover(); 
  
        return single_instance; 
    } 
    
    public static void main(String... args) {
    	MouseMover tmp=getInstance();
    	
    	System.out.println(tmp.getDate());
    	tmp.delayThread();
    	try {
    		tmp.loadProperties("KeepAW.cfg");
			tmp.doTheStuff();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void doTheStuff() throws Exception 
    {
    	Robot robot = new Robot();
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
     
        Runnable myRunnable = new Runnable()
        {
        	public void run(){
        		while(true) {
        			//System.out.println("cmd:");
                    long then = System.currentTimeMillis();
                    String line = scanner.nextLine();
                    
                    clrscr();
                    
                    output=line;
//                    long now = System.currentTimeMillis();
//                    System.out.printf("Waited %.3fs for user input%n", (now - then) / 1000d);
//                    System.out.printf("User input was: %s%n", line);
                    
                    if(line.equalsIgnoreCase("exit"))
                    	System.exit(0);
                    else if(line.equalsIgnoreCase("reload"))
                    	loadProperties("KeepAW.cfg");
                    else if(line.equalsIgnoreCase("cls"))
                    	clrscr();
                    else if(line.equalsIgnoreCase("printinfo=true"))
                    	printInfo=true;
                    else if(line.equalsIgnoreCase("printinfo=false"))
                    	printInfo=false;
                    else if(line.equalsIgnoreCase("testcmd"))
                    	testCMDs();
                    else if(line.equalsIgnoreCase("help")) {
                    	printCMDs();
                    	delayThread();
                    }
                    else
                    	System.out.printf("command not recognized!");
                    	
        			/*
	                System.out.println("Runnable running");
	                try {
						Thread.sleep(FIVE_SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
        		}
            }
        };
	    Thread thread = new Thread(myRunnable);
	//    thread.setName("ComClientDoActionThread");
	    thread.start();
        
        while (true) {
            //robot.mouseMove(random.nextInt(MAX_X), random.nextInt(MAX_Y));
        	Point p = MouseInfo.getPointerInfo().getLocation();
            robot.mouseMove(p.x+DIST, p.y+DIST);
            robot.mouseMove(p.x, p.y);
            //System.out.println(output);
            if(printInfo)
            	System.out.println(getDate());
            Thread.sleep(TIME_INT);
        }
    }
    public static void clrscr(){

        //Clears Screen in java

       try {

            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                try {
					Runtime.getRuntime().exec("clear");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

        } catch (IOException | InterruptedException ex) {}
    }
    private void printCMDs() {
    	System.out.println("Available commands: ");
    	System.out.println("help: Prints available commands.");
    	System.out.println("cls: clears console.");
    	System.out.println("reload: reloads config parameters.");
    	System.out.println("printinfo=true: enables date/time output to console at each intervall.");
    	System.out.println("printinfo=false: diables date/time output to console at each intervall.");
    	System.out.println("exit: terminates program.");
    	
    }
    private String getDate() {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    	String dateString = format.format( new Date()   );
    	return dateString;
    }
    
    private void loadProperties(String propFileName)
	{
		prop=new Properties();
		InputStream input = null;
		try {
			input=new FileInputStream(propFileName);
			prop.load(input);
		// get the property value and print it out
			//System.out.println(prop.getProperty("TIME"));
			if(prop.getProperty("TIME")!=null) {
				TIME_INT=Integer.parseInt(prop.getProperty("TIME"));
				System.out.println("TIME_INT: "+TIME_INT+"ms");
			}
			//System.out.println(prop.getProperty("DIST"));
			if(prop.getProperty("DIST")!=null) {
				DIST=Integer.parseInt(prop.getProperty("DIST"));
				System.out.println("DIST: "+DIST+"px");
			}
				
		//	System.out.println(prop.getProperty("SERVER_PORT"));
		//	System.out.println(prop.getProperty("CLIENT_NAME"));
			
//			SERVER_HOSTNAME = prop.getProperty("SERVER_HOSTNAME");
//		    SERVER_PORT = Integer.parseInt(prop.getProperty("SERVER_PORT"));
//		    COMMANDS = prop.getProperty("COMMANDS");
//		    LOGCONFIGFILEPATH=prop.getProperty("LOGCONFIGFILEPATH");
//		    CLIENT_NAME=prop.getProperty("CLIENT_NAME");
//		    
//		    if(prop.getProperty("SECURE_SERVER_PORT")!=null)
//		    	SECURE_SERVER_PORT= Integer.parseInt(prop.getProperty("SECURE_SERVER_PORT"));
//		    
//		    keystoreFile=prop.getProperty("KEYSTORE");
//		    keystorePassWD=prop.getProperty("KEYSTOREPASSWORD");
//			clientPassWD=prop.getProperty("CLIENTPASSWORD");
		    
		} 
		catch(FileNotFoundException e) {
			System.out.println("java.io.FileNotFoundException: "+propFileName+" file not found. Using default values.");
			System.out.println("TIME_INT: "+TIME_INT+"ms, DIST: "+DIST+"px");
			//System.out.println("DIST: "+DIST+"px");
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			//LOG.error(e.toString());
			e.printStackTrace();
			//System.exit(1);
		}
		finally 
		{
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
    private void delayThread()
    {
    	
    	Runnable myRunnable = new Runnable()
        {
        	public void run(){
        		//while(true) {
        			System.out.println("cmd:");
                    
                    	
        			
	                //System.out.println("Runnable running");
	                try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                clrscr();
	                System.out.println(getDate());
	                System.out.println("cmd:");
					
        	//	}
            }
        };
        Thread thread = new Thread(myRunnable);
    	thread.start();
    }
    
    //Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep"); //Sleep Computer
	private void testCMDs()
	{
		String op_sys = System.getProperty("os.name");
		System.out.println("OS:"+op_sys);
		try {
			Runtime.getRuntime().exec("Rundll32.exe user32.dll,LockWorkStation"); //Ilåser datorn
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}