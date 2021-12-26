package arithmetics;
import java.util.*;
import java.text.ParseException;
import util.base.*;
public class GeneratedParser {
	public GeneratedEnum curToken;
	public final GeneratedLexer lexer;
	public GeneratedParser(String text) {
		lexer = new GeneratedLexer(text);
		curToken = lexer.next();
	}
	
	public static class EAttributes extends Node {
		public EAttributes(String name) {
			super(name);
		}
		
		public Integer val;
		public Integer acc;
		
	}
	public EAttributes e() throws ParseException {
		Integer val = null;
		Integer acc = null;
		TAttributes t = null;
		VAttributes v = null;
		EpAttributes ep = null;
		EAttributes _inner = new EAttributes("e");
		t = t();
		_inner.children.add(t);
		val = t.val; acc = t.val;
		
		ep = ep(acc);
		_inner.children.add(ep);
		val = ep.val; acc = val;
		
		v = v(acc);
		_inner.children.add(v);
		val = v.val;
		
		
		_inner.val = val;
		_inner.acc = acc;
		return _inner;
	}
	
	public static class EpAttributes extends Node {
		public EpAttributes(String name) {
			super(name);
		}
		
		public Integer val;
		public Integer myAcc;
		public Integer acc;
	}
	public EpAttributes ep(Integer acc) throws ParseException {
		Integer val = null;
		Integer myAcc = null;
		GeneratedEnum SUB = null;
		GeneratedEnum EPS = null;
		GeneratedEnum ADD = null;
		TAttributes t = null;
		EpAttributes ep = null;
		EpAttributes _inner = new EpAttributes("ep");
		switch (curToken.token) {
			case ADD -> {
				ADD = curToken;
				_inner.children.add(new Node(curToken.toString()));
				curToken = lexer.next();
				
				t = t();
				_inner.children.add(t);;myAcc = acc + t.val;ep = ep(myAcc);
				_inner.children.add(ep);;val = ep.val;
			}
			case SUB -> {
				SUB = curToken;
				_inner.children.add(new Node(curToken.toString()));
				curToken = lexer.next();
				
				t = t();
				_inner.children.add(t);;myAcc = acc - t.val;;ep = ep(myAcc);
				_inner.children.add(ep);;val = ep.val;
			}
			case EPS -> {
				EPS = curToken;
				
				val = acc;
			}
			default -> {val = acc;}
		}
		_inner.acc = acc;
		_inner.val = val;
		_inner.myAcc = myAcc;
		return _inner;
	}
	
	public static class VAttributes extends Node {
		public VAttributes(String name) {
			super(name);
		}
		
		public Integer val;
		public Integer myAcc;
		public Integer acc;
	}
	public VAttributes v(Integer acc) throws ParseException {
		Integer val = null;
		Integer myAcc = null;
		GeneratedEnum EPS = null;
		GeneratedEnum RSHIFT = null;
		TAttributes t = null;
		VAttributes v = null;
		GeneratedEnum LSHIFT = null;
		VAttributes _inner = new VAttributes("v");
		switch (curToken.token) {
			case LSHIFT -> {
				LSHIFT = curToken;
				_inner.children.add(new Node(curToken.toString()));
				curToken = lexer.next();
				
				t = t();
				_inner.children.add(t);;myAcc = acc << t.val;v = v(myAcc);
				_inner.children.add(v);;val = v.val;
			}
			case RSHIFT -> {
				RSHIFT = curToken;
				_inner.children.add(new Node(curToken.toString()));
				curToken = lexer.next();
				
				t = t();
				_inner.children.add(t);;myAcc = acc >> t.val;;v = v(myAcc);
				_inner.children.add(v);;val = v.val;
			}
			case EPS -> {
				EPS = curToken;
				
				val = acc;
			}
			default -> {val = acc;}
		}
		_inner.acc = acc;
		_inner.val = val;
		_inner.myAcc = myAcc;
		return _inner;
	}
	
	public static class TAttributes extends Node {
		public TAttributes(String name) {
			super(name);
		}
		
		public Integer val;
		public Integer myAcc;
		
	}
	public TAttributes t() throws ParseException {
		Integer val = null;
		Integer myAcc = null;
		TpAttributes tp = null;
		FAttributes f = null;
		TAttributes _inner = new TAttributes("t");
		f = f();
		_inner.children.add(f);
		myAcc = f.val;
		
		tp = tp(myAcc);
		_inner.children.add(tp);
		val = tp.val;
		
		
		_inner.val = val;
		_inner.myAcc = myAcc;
		return _inner;
	}
	
	public static class TpAttributes extends Node {
		public TpAttributes(String name) {
			super(name);
		}
		
		public Integer val;
		public Integer myAcc;
		public Integer acc;
	}
	public TpAttributes tp(Integer acc) throws ParseException {
		Integer val = null;
		Integer myAcc = null;
		GeneratedEnum DIV = null;
		GeneratedEnum EPS = null;
		GeneratedEnum MUL = null;
		TpAttributes tp = null;
		FAttributes f = null;
		TpAttributes _inner = new TpAttributes("tp");
		switch (curToken.token) {
			case MUL -> {
				MUL = curToken;
				_inner.children.add(new Node(curToken.toString()));
				curToken = lexer.next();
				
				f = f();
				_inner.children.add(f);;myAcc = acc * f.val;;tp = tp(myAcc);
				_inner.children.add(tp);;val = tp.val;
			}
			case DIV -> {
				DIV = curToken;
				_inner.children.add(new Node(curToken.toString()));
				curToken = lexer.next();
				
				f = f();
				_inner.children.add(f);;myAcc = acc / f.val;;tp = tp(myAcc);
				_inner.children.add(tp);;val = tp.val;
			}
			case EPS -> {
				EPS = curToken;
				
				val = acc;
			}
			default -> {val = acc;}
		}
		_inner.acc = acc;
		_inner.val = val;
		_inner.myAcc = myAcc;
		return _inner;
	}
	
	public static class FAttributes extends Node {
		public FAttributes(String name) {
			super(name);
		}
		
		public Integer val;
		
	}
	public FAttributes f() throws ParseException {
		Integer val = null;
		EAttributes e = null;
		GeneratedEnum SUB = null;
		GeneratedEnum LPAR = null;
		GeneratedEnum NUM = null;
		FAttributes f = null;
		GeneratedEnum RPAR = null;
		FAttributes _inner = new FAttributes("f");
		switch (curToken.token) {
			case LPAR -> {
				LPAR = curToken;
				_inner.children.add(new Node(curToken.toString()));
				curToken = lexer.next();
				
				e = e();
				_inner.children.add(e);;val = e.val;;RPAR = curToken;
				if (GeneratedEnum.EToken.RPAR != curToken.token) {
					throw new ParseException("UNEXPECTED TOKEN(((", lexer.position());
				}
				curToken = lexer.next();
				
				_inner.children.add(new Node("RPAR"));
			}
			case NUM -> {
				NUM = curToken;
				_inner.children.add(new Node(curToken.toString()));
				curToken = lexer.next();
				
				val = Integer.parseInt(NUM.text);
			}
			case SUB -> {
				SUB = curToken;
				_inner.children.add(new Node(curToken.toString()));
				curToken = lexer.next();
				
				f = f();
				_inner.children.add(f);;val = -f.val;
			}
			default -> throw new ParseException("Unexpected token", lexer.position());
		}
		
		_inner.val = val;
		return _inner;
	}
	
	
	public static class Node extends BaseNode {
		public Node(String name) {
			super(name);
		}
	}
	
}