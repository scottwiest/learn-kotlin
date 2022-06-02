import kotlin.collections.HashMap

fun main() {
    val secretWord = "BLAST"
    val userInput = "LASSO"
    println(returnMatchString(userInput, secretWord))
    println(anagramFinder(userInput, secretWord))
}

fun anagramFinder(userInput: String, storedValue: String): Boolean{
    if(userInput.length != storedValue.length)
        return false

    val storedMap = hashMapOf<Char?, Int?>()
    val userMap = hashMapOf<Char?, Int?>()
    for(char in storedValue.lowercase().filter { !it.isWhitespace() }){
        when(storedMap[char]){
            null -> storedMap[char] = 1
            else -> storedMap[char]!!.plus(1)
        }
    }
    for(char in userInput.lowercase().filter { !it.isWhitespace() }){
        when(userMap[char]){
            null -> userMap[char] = 1
            else -> userMap[char]!!.plus(1)
        }
    }

    return storedMap == userMap
}

fun returnMatchString(userInput: String, secretWord: String): String{
    var result = ""
    val secretMap = hashMapOf<Char?, Int?>()
    val removedMap = hashMapOf<Char?, MutableList<Int>?>()

    for(char in secretWord)
        increment(secretMap, char)

    for(i in userInput.indices) {
        if((userInput[i] in secretMap) or (userInput[i] in removedMap)){
            if(userInput[i] == secretWord[i]) {
                result += 'G'
                if(userInput[i] in removedMap){
                    if(result[removedMap[userInput[i]]!!.first()] == 'Y'){
                        val chars = result.toCharArray()
                        chars[removedMap[userInput[i]]!!.first()] = 'R'
                        result = String(chars)
                        when(removedMap[userInput[i]]!!.size) {
                            0, 1 -> removedMap.remove(userInput[i])
                            else -> removedMap[userInput[i]]!!.removeFirst()
                        }
                    }
                }
            }
            else
                result += 'Y'
            if(userInput[i] in secretMap) {
                decrement(secretMap, userInput[i])
                increment(removedMap, userInput[i], i)
            }
        }
        else
            result += 'R'
    }
    return result
}

fun <K> increment(map: HashMap<K, Int?>, key: K) {
    when(val count = map[key]) {
        null -> map[key] = 1
        else -> map[key] = count + 1
    }
}

fun <K> increment(map: HashMap<K, MutableList<Int>?>, key: K, index: Int) {
    when(map[key]) {
        null -> map[key] = mutableListOf(index)
        else -> map[key]?.add(index)
    }
}

fun <K> decrement(map: HashMap<K, Int?>, key: K) {
    when(val count = map[key]) {
        null, 1 -> map.remove(key)
        else -> map[key] = count - 1
    }
}