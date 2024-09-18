package com.jetbrains.kmpapp.screens.seemeHome


fun readJsonFromFile(context: Context, fileName: String): Person? {
    val file = File(context.filesDir, fileName)
    return try {
        FileReader(file).use { reader ->
            Gson().fromJson(reader, Person::class.java)
        }
    } catch (e: Exception) {
        // Handle exceptions (e.g., file not found, parsing errors)
        e.printStackTrace()
        null
    }
}

fun readJsonFromAssets(context: Context, fileName: Int): String? {
    return try {
        context.resources.openRawResource(fileName).bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        // Handle exceptions
        e.printStackTrace()
        null
    }
}

fun getHomeFromJson(context: Context, fileName: Int): HomeRes? {
    val jsonString = readJsonFromAssets(context, fileName)
    return if (jsonString != null) {
        Gson().fromJson(jsonString, HomeRes::class.java)
    } else {
        null
    }
}

inline fun logMsg(closer: () -> String) {
        println("logMsg :  ${closer()}")
}

fun getFileFromRaw(context: Context, rawResourceId: Int): InputStream {
    return context.resources.openRawResource(rawResourceId)
}
fun createFileFromInputStream(inputStream: InputStream, destinationFile: File): Boolean {
    return try {
        FileOutputStream(destinationFile).use { outputStream ->
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            outputStream.flush()
        }
        true // Success
    } catch (e: Exception) {
        // Handle exceptions (e.g., file writing errors)
        false // Failure
    }
}
//fun getFileFromInputStream(context: Context, inputStream: InputStream): InputStream {
//    val destinationFile = File(context.filesDir, "my_new_file.txt") // Or any desired location
//
//    createFileFromInputStream(inputStream, destinationFile)
//}