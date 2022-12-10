# PLC-final
## Grammar
- S = \<program>
- V = { 
    <br>\<program>, \<stmt>, \<ifstmt>, \<elsestmt>, \<forloop>, \<whileloop>, \<dowhile>, \<switch>, \<case>, \<expr>, \<assertion>, \<term>, \<factor>, \<boolexpr>, \<boolop>, \<unary>, \<var>, \<ident>, \<type>, \<declare>, \<return>
	<br>}
- ∑ = { 
    <br>tree, leaf, seed, twig, trunk, stick, apple,
		<br>orange, climb, branch, growfor, grow, root, eat, stop,
		<br><, >, <=, >=, ==, !=, (, ), {, }, ,, =, +, -, *, /,
		<br>%,:
	<br>}
- R = [
<br>\<program> --> tree{ \<stmt> }

<br>\<stmt> --> \<func>\<stmt> | \<call> leaf\<stmt> | \<forloop>\<stmt> | \<whileloop>\<stmt> | \<dowhile>\<stmt> | \<ifstmt>\<stmt> | \<switch>\<stmt> | \<var> leaf \<stmt> | \<assertion> leaf \<stmt> | null

<br>\<func> --> sapling \<type> id(\<param>) : \<statement> \<return> :
<br>\<param> --> \<type> id\<param>|,\<type> id \<param>|null
<br>\<return> --> give \<expr> leaf \<statement>
<br>\<call> --> planted id(\<call2>)
<br>\<call2> --> \<term>\<call2>|, \<term>\<call2>|null

<br>\<forloop> --> growfor(\<var>,\<boolexpr>,\<expr>) : \<stmt> stop
<br>\whileloop> --> grow(\<boolexpr>) : \<stmt> stop
<br>\<dowhile> --> root : \<stmt> stop grow(\<boolexpr>)

<br>\<ifstmt> --> apple(\<boolexpr>) : \<stmt> \<elsestmt> eat
<br>\<elsestmt> --> orange : \<stmt>|null

<br>\<switch> --> climb(id) : \<case> stop
<br>\<case> --> branch val : \<stmt> \<case>|null

<br>\<var> --> \<type> id \<declare>
<br>\<type> --> seed|stick|twig|trunk|petal|flower|planted|sap
<br>\<declare> --> = \<expr>|null
<br>\<assertion> --> id = \<expr>

<br>\<boolexpr> --> \<expr> \<op> \<expr> | \<unary> \<expr>
<br>\<op> --> >>|>>>=|<<|<<<=|===|!!!=
<br>\<unary> --> !!!|null

<br>\<expr> --> (\<expr>)|\<term>\<factor>
<br>\<factor> --> + \<expr>|- \<expr>|* \<expr>|/ \<expr>|% \<expr>|*^ \<expr>|null
<br>\<term> --> id|val|\<call>

	<br>]
## RegEx
<br>Identifier: ([a-z]_?){6,8}
<br>Natural: [0-9]+
<br>Real: [0-9]+\.[0-9]+
<br>Boolean: yes|no
