/*Project Name is : Calculator
Authors : Mostafa / Islam/ Mariem/ Esraa/Donia
*/


package calculator_project;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jssc.SerialPort;
import static jssc.SerialPort.MASK_RXCHAR;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;


public class TestJSSC extends Application {
 
    //Global Variables
    
    public static Button add;
    public static Button sub;
    public static Button div;
    public static Button mul;
    public static Button dot;
    public static Button equal;
    gnu.io.SerialPort serialPort;
    Integer i;
    Float data = 0f;
    int nflag = 0, firstDot = 1;
    char oper = '+';
    String inputLine;
    TextArea opField = new TextArea();
    Label resLabel = new Label("0");
    static Thread g;
    SerialPort arduinoPort = null;
    ObservableList<String> portList;
    Label labelValue;
    
    //To detect Port needed
    private void detectPort(){
         
        portList = FXCollections.observableArrayList();
 
        String[] serialPortNames = SerialPortList.getPortNames();
        for(String name: serialPortNames){
            System.out.println(name);
            portList.add(name);
        }
    }
    
    //Public Function For all Operations
     public void operate() {
                switch (oper){
                    case '+':
                        
                        data += Float.parseFloat(resLabel.getText());
                        break;
                    case '-':
                        
                        data -= Float.parseFloat(resLabel.getText());
                        break;
                    case '*':
                        
                        data *= Float.parseFloat(resLabel.getText());
                        break;
                    case '/':
                        
                        data /= Float.parseFloat(resLabel.getText());
                        break;
                }
                resLabel.setText(data.toString());
            }
    
     //equal case
     public void equal() {
                switch (oper){
                    case '+':
                        data += Float.parseFloat(resLabel.getText());
                        break;
                    case '-':
                        data -= Float.parseFloat(resLabel.getText());
                        break;
                    case '*':
                        data *= Float.parseFloat(resLabel.getText());
                        break;
                    case '/':
                        data /= Float.parseFloat(resLabel.getText());
                        break;
                }
                resLabel.setText(data.toString());
                oper = '+';
                data=0f;
            }
     //Start Function of Our Application 
    public void start(Stage s) throws Exception{
        
        detectPort();
        connectArduino("COM4");
        
        /*TextField*/
        opField.setId("textArea");
        opField.setDisable(true);
        opField.setMaxHeight(175);
        opField.setMinHeight(175);
        opField.setText("");
        
        /*Result Label*/
        resLabel.setId("label");
        
        /*Result Pane*/
        StackPane resPane = new StackPane(opField, resLabel);
        resPane.setAlignment(Pos.BOTTOM_RIGHT);
        
        /*Operations*/
        Button add = new Button("+");
        add.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add.setId("opButton");
        add.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {  
                opField.setText(opField.getText() + resLabel.getText()+"+");
                nflag = 0;
                operate();
                oper = '+';
                firstDot = 1;                
            }
        });
        
        Button sub = new Button("-");
        sub.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        sub.setId("opButton");
        sub.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                opField.setText(opField.getText() + resLabel.getText()+"-");
                nflag = 0;
                operate();
                oper = '-';
                firstDot = 1;     
            }
        });
        
        Button div = new Button("/");
        div.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        div.setId("opButton");
        div.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                opField.setText(opField.getText() + resLabel.getText()+"/");
                nflag = 0;
                operate();
                oper = '/';
                firstDot = 1;
            }

            
        });
        
        /*Mul*/
        mul = new Button("*");
        mul.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        mul.setId("opButton");
        mul.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                opField.setText(opField.getText() + resLabel.getText()+"*");
                nflag = 0;
                operate();
                oper = '*';
                firstDot = 1;
            }
        });
        
        Button dot = new Button(".");
        dot.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        dot.setId("opButton");
        dot.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (firstDot == 1)
                {
                    if(nflag == 0)
                    {
                        resLabel.setText("0");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText() + ".");
                    firstDot=0;
                }
            }
        });
        
        Button equal = new Button("=");
        equal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        equal.setId("opButton");
        equal.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                nflag = 0;
                opField.setText("");
                equal();
                data = 0f;
            }

            private void equal() {
                switch (oper){
                    case '+':
                        data += Float.parseFloat(resLabel.getText());
                        break;
                    case '-':
                        data -= Float.parseFloat(resLabel.getText());
                        break;
                    case '*':
                        data *= Float.parseFloat(resLabel.getText());
                        break;
                    case '/':
                        data /= Float.parseFloat(resLabel.getText());
                        break;
                }
                resLabel.setText(data.toString());
                oper = '+';
            }
        });
        
        GridPane buttonsPane = new GridPane();
        
        for (int col = 0 ; col < 4 ; col++ ){
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setHgrow(Priority.ALWAYS) ;
            colConst.setFillWidth(true);
            buttonsPane.getColumnConstraints().add(colConst); 
        }
        
        for (int row = 0 ; row < 4 ; row++ ){
            RowConstraints rowConst = new RowConstraints();
            rowConst.setVgrow(Priority.ALWAYS) ;
            rowConst.setFillHeight(true);
            buttonsPane.getRowConstraints().add(rowConst); 
        }
        
        
        /*Numbers Buttons*/
        Button zero = new Button("0");
        zero.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        zero.setId("numButton");
        zero.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    if(nflag == 0)
                    {
                        resLabel.setText("");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText()+"0");
                }
            }
            );
        
        
        Button one = new Button("1");
        one.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        one.setId("numButton");
        one.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    if(nflag == 0)
                    {
                        resLabel.setText("");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText()+"1");
                   
                }
            }
            );

        Button two = new Button("2");
        two.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        two.setId("numButton");
        two.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    
                    if(nflag == 0)
                    {
                        resLabel.setText("");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText()+"2");
                    
                }
            }
            );
        
        Button three = new Button("3");
        three.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        three.setId("numButton");
        three.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    if(nflag == 0)
                    {
                        resLabel.setText("");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText()+"3");
                }
            }
            );
        
        Button four = new Button("4");
        four.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        four.setId("numButton");
        four.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    if(nflag == 0)
                    {
                        resLabel.setText("");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText()+"4");
                }
            }
            );
        
        Button five = new Button("5");
        five.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        five.setId("numButton");
        five.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    if(nflag == 0)
                    {
                        resLabel.setText("");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText()+"5");
                }
            }
            );
        
        Button six = new Button("6");
        six.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        six.setId("numButton");
        six.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    if(nflag == 0)
                    {
                        resLabel.setText("");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText()+"6");
                }
            }
            );
        
        Button seven = new Button("7");
        seven.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        seven.setId("numButton");
        seven.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    if(nflag == 0)
                    {
                        resLabel.setText("");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText()+"7");
                }
            }
            );
        
        Button eight = new Button("8");
        eight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        eight.setId("numButton");
        eight.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    if(nflag == 0)
                    {
                        resLabel.setText("");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText()+"8");
                }
            }
            );
        
        Button nine = new Button("9");
        nine.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        nine.setId("numButton");
        nine.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    if(nflag == 0)
                    {
                        resLabel.setText("");
                    }
                    nflag = 1;
                    resLabel.setText(resLabel.getText()+"9");
                }
            }
            );
        
        buttonsPane.addRow(0, seven, eight, nine, add);
        buttonsPane.addRow(1, four, five, six, sub);
        buttonsPane.addRow(2, one, two, three, div);
        buttonsPane.addRow(3, dot, zero, equal, mul);
               
       
        /*CopyRights Label*/
        Label copyRights = new Label("All rights reserved @ Team 4 ES");
        
        /*Pane*/
        BorderPane mainPane = new BorderPane();
        mainPane.setTop(resPane);
        mainPane.setCenter(buttonsPane);
        mainPane.setBottom(copyRights);
        
        /*Scene*/
        Scene mainScene = new Scene(mainPane, 550, 750);
        mainScene.getStylesheets().add(getClass().getResource("calcStyle.css").toString());
        
        /*Stage*/
        s.setTitle("SuperCalc");
        s.setScene(mainScene);
        s.initStyle(StageStyle.UTILITY);
        s.show();
    }
  

    public boolean connectArduino(String port){
        
        System.out.println("connectArduino");
        
        boolean success = false;
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(
            SerialPort.BAUDRATE_9600,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if(serialPortEvent.isRXCHAR()){
                    try {
                        String st = serialPort.readString(serialPortEvent
                               .getEventValue()).replaceAll("\r","").replaceAll("\n", "");
                        System.out.print(st);
                        
                        //Update label in ui thread
                        Platform.runLater(() -> {
                            
                      if(st.equals("+"))
                      {
                            opField.setText(opField.getText()+resLabel.getText()+st);
                            nflag = 0;
                            operate();
                            resLabel.setText(data.toString());
                            oper = '+';
                            firstDot = 1;
                            resLabel.setText(data.toString());
                        
                      }
                      else if(st.equals("-"))
                      {
                            opField.setText(opField.getText()+resLabel.getText()+st);
                            nflag = 0;
                            operate();
                            oper = '-';
                            firstDot = 1;

                      }
                      else if(st.equals("*"))
                      {
                            opField.setText(opField.getText()+resLabel.getText()+st);
                            nflag = 0;
                            operate();
                            oper = '*';
                            firstDot = 1;
                      
                      }
                      else if(st.equals("/"))
                      {
                            opField.setText(opField.getText()+resLabel.getText()+st);
                            //opField.setText(opField.getText() + resLabel.getText()+"/");
                            nflag = 0;
                            operate();
                            oper = '/';
                            firstDot = 1;
                      
                      }
                      else if(st.equals("="))
                      {
                            nflag = 0;
                            opField.setText("");
                            equal();
                            data = 0f;
                      
                      }
                      else if(st.equals("."))
                      {
                            if (firstDot == 1)
                            {
                                if(nflag == 0)
                                {
                                    resLabel.setText("0");
                                }
                                nflag = 1;
                                resLabel.setText(resLabel.getText() + ".");
                                firstDot=0;
                            }
                      }
                      else if (st.equals("0") || st.equals("1") || st.equals("2") ||st.equals("3") ||st.equals("4") ||st.equals("5") ||st.equals("6") ||st.equals("7") || st.equals("8") || st.equals("9"))
                      {
                          if(nflag == 0)
                            {
                                resLabel.setText("");
                            }
                          
                            nflag = 1;
                            resLabel.setText(resLabel.getText()+st);
                            
                      }
                      
                        });
                        
                   } catch (SerialPortException ex) {
                        System.out.println("Sorry the keypad is not connected");
                    }
                    
                }
            });
            
            arduinoPort = serialPort;
            success = true;
        } catch (SerialPortException ex) {
            System.out.println("Sorry the keypad is not connected");
            System.out.println("please connect the Hardware and restart the program");
        }

        return success;
    }
    
    public void disconnectArduino(){
        
        System.out.println("disconnectArduino()");
        if(arduinoPort != null){
            try {
                arduinoPort.removeEventListener();
                
                if(arduinoPort.isOpened()){
                    arduinoPort.closePort();
                }
                
            } catch (SerialPortException ex) {
                Logger.getLogger(TestJSSC.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        disconnectArduino();
        super.stop();
    }
            
    public static void main(String[] args) throws Exception {
        
        launch(args);
    }
    
}