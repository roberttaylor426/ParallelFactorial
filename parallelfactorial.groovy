@Grab(group='org.codehaus.gpars', module='gpars', version='1.1.0')
import static groovyx.gpars.GParsPool.withPool

class MultiFactorialCalculator {

    def calculateFactorialValues(numbers) {
        withPool(numbers.size()) {
            measureTime {
                numbers.collectParallel { number ->
                    measureTime { factorial(number) }.'timeTaken'
                }
            } 
        }
    }
    
    def measureTime(closure) {
        long startTime = System.currentTimeMillis()
        def result = closure.call()
        def timeTaken = System.currentTimeMillis() - startTime
        [result: result, timeTaken: timeTaken]
    }
    
    def factorial(n) {
        factorialAcc(n)
    }
    
    def factorialAcc = { n, BigDecimal acc = 1 ->
        if (n == 0) return acc    
        factorialAcc.trampoline n - 1, acc * n
    }.trampoline()
}

new MultiFactorialCalculator().calculateFactorialValues([100000] * 4)
