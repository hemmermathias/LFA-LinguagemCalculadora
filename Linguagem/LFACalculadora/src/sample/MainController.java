package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lib.*;

import javafx.scene.control.TextArea;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainController extends Semantico {

    @FXML
    private TextArea txtInput;
    @FXML
    private TextArea txtStack;
    @FXML
    private TextArea txtLog;
    @FXML
    private TextArea txtResult;

    Stack<Integer> data = new Stack();
    Map<String, Integer> variables = new HashMap<String, Integer>();
    String currentVariable;

    public void Evaluate(ActionEvent e){

        txtStack.setText("");
        txtLog.setText("");
        txtResult.setText("");

        data.clear();
        variables.clear();
        currentVariable = "";

        Sintatico sintatic = new Sintatico();
        Lexico data = new Lexico(txtInput.getText());
        try {
            sintatic.parse(data, this);
        } catch (LexicalError lexicalError) {
            txtLog.setText("Entrada de dados fora do padrão permitido!\n" + "Posição: " + lexicalError.getPosition() + '\n' + "Caracter: " +  lexicalError.getMessage());
        } catch (SyntaticError er2) {
            txtLog.setText("Erro de sintaxe!\n" + er2.getMessage() + "\n" + er2.getPosition());
        } catch (SemanticError er3) {
            txtLog.setText("Erro de sintaxe 3!\n" + er3.getMessage() + "\n" + er3.getPosition());
        }
    }

    public void LogVariables(){
        String log = "";
        for(Map.Entry<String, Integer> var : variables.entrySet()){
            log += "var " + var.getKey() + " = " + var.getValue() + "\n";
        }
        txtStack.setText(log);
    }

    public void executeAction(int action, Token token)	throws SemanticError
    {
        switch (action){
            case 1: // Variable attribution began
                currentVariable = token.getLexeme();
                break;
            case 2: // Variable Read
                data.push(variables.get(token.getLexeme()));
                break;
            case 3: // Integer Value
                data.push(Integer.parseInt(token.getLexeme(), 10));
                break;
            case 4: // Variable attribution end
                variables.put(currentVariable, data.pop());
                LogVariables();
                break;
            case 5: // Initiated show method
                break;
            case 6: //Ended show method
                String res = Integer.toString(data.pop());
                txtResult.setText(res);
                System.out.println(res);
                break;
            case 10: // +
                int a, b;
                a = data.pop();
                b = data.pop();
                data.push(a+b);
                break;
            case 11: // -
                a = data.pop();
                b = data.pop();
                data.push(a-b);
                break;
            case 12: // *
                a = data.pop();
                b = data.pop();
                data.push(a*b);
                break;
            case 13: // /
                a = data.pop();
                b = data.pop();
                data.push(a/b);
                break;
            case 14: // ^
                a = data.pop();
                b = data.pop();
                data.push((int)Math.pow(a,b));
                break;
        }
    }
}
