package com.mandamong.server.common.util.parser

import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

object SpringELParser {

    private val parser: ExpressionParser = SpelExpressionParser()

    fun getDynamicValue(
        parameterNames: Array<String>,
        args: Array<Any>,
        key: String,
    ): String {
        val context = StandardEvaluationContext()
        for (i in parameterNames.indices) {
            context.setVariable(parameterNames[i], args[i])
        }
        return parser.parseExpression(key).getValue(context).toString()
    }

}
