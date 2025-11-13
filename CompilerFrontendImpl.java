import java.util.EnumMap;


public class CompilerFrontendImpl implements CompilerFrontend {
    boolean debug;
    LexerImpl lexer; 

    public CompilerFrontendImpl(boolean debug) {
        this.debug = debug;
        lexer = new LexerImpl();
        buildAutomata();
    }


    @Override
    public TokenList run_lexer(String input) {
        TokenList all = lexer.scan(input);
       
        TokenList head = null;
        TokenList tail = null;
        TokenList cur = all;
        while (cur != null) {
            if (cur.elem.ty != TokenType.WHITE_SPACE) {
                TokenList node = new TokenList(cur.elem, null);
                if (head == null) {
                    head = node;
                    tail = node;
                } else {
                    tail.rest = node;
                    tail = node;
                }
            }
            cur = cur.rest;
        }
        return head;
    }

    @Override
    public Expr run(String input) throws Exception {
        TokenList tokens = run_lexer(input);
        ParserImpl parser = new ParserImpl();
        return parser.parse(tokens);
    }

   
    private void buildAutomata() {
       
        Automaton a_num = new AutomatonImpl();
       
        a_num.addState(0, true, false);
        a_num.addState(2, false, false);
        a_num.addState(3, false, true);
       
        for (char d = '0'; d <= '9'; d++) {
            a_num.addTransition(0, d, 0); 
        }
       
        a_num.addTransition(0, '.', 2);
        
        for (char d = '0'; d <= '9'; d++) {
            a_num.addTransition(2, d, 3);
            a_num.addTransition(3, d, 3); 
        }


        Automaton a_plus = singleCharAutomaton('+');

        Automaton a_minus = singleCharAutomaton('-');

        Automaton a_times = singleCharAutomaton('*');

        Automaton a_div = singleCharAutomaton('/');

        Automaton a_lparen = singleCharAutomaton('(');

        Automaton a_rparen = singleCharAutomaton(')');

       
        Automaton a_ws = new AutomatonImpl();
        a_ws.addState(0, true, false);
        a_ws.addState(1, false, true);
        a_ws.addTransition(0, ' ', 1);
        a_ws.addTransition(0, '\n', 1);
        a_ws.addTransition(0, '\r', 1);
        a_ws.addTransition(0, '\t', 1);
        a_ws.addTransition(1, ' ', 1);
        a_ws.addTransition(1, '\n', 1);
        a_ws.addTransition(1, '\r', 1);
        a_ws.addTransition(1, '\t', 1);

        lexer.add_automaton(TokenType.NUM, a_num);
        lexer.add_automaton(TokenType.PLUS, a_plus);
        lexer.add_automaton(TokenType.MINUS, a_minus);
        lexer.add_automaton(TokenType.TIMES, a_times);
        lexer.add_automaton(TokenType.DIV, a_div);
        lexer.add_automaton(TokenType.LPAREN, a_lparen);
        lexer.add_automaton(TokenType.RPAREN, a_rparen);
        lexer.add_automaton(TokenType.WHITE_SPACE, a_ws);
    }

    private Automaton singleCharAutomaton(char c) {
        Automaton a = new AutomatonImpl();
        a.addState(0, true, false);
        a.addState(1, false, true);
        a.addTransition(0, c, 1);
        return a;
    }
}
