package com.xs.lightpuzzle.demo.a_kotlin_demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xs.lightpuzzle.R
import kotlinx.android.synthetic.main.activity_practice_kotlin.*
import java.lang.IllegalArgumentException

/**
 * Author: xs
 * Create on: 2019/07/12
 * Description: _
 */
class PracticeKotlinActivity : AppCompatActivity() {

    var score = 2

    var otherName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_kotlin)
        btnTest.setOnClickListener {
            txtTest.text = "kotlin测试成功"
        }
        btnTestVararg.setOnClickListener {
            testObject()
//            doSomething(1, 2, 3, 4, 5)
        }


        var test : TestKotlin = TestKotlin()
        test.isVisible = false
    }

    /**
     * fun 修饰函数
     * vararg 函数的变长参数
     * filter 过滤 forEach 遍历
     */
    private fun doSomething(vararg numbers: Int) {
        numbers.filter { it > 2 }
                .forEach { println(it) }
        for (num in numbers) {
            println(num)
        }
        val text = if (score > 5) "x > 5" else "x < 5"
        val length = otherName?.length
        val message = "My name is: $text $length"

        var result = 3.triple()

        var name: String? = "haha" // ? 可为null || safe call
        name = if (length == 0) null else name // 三元表达式
        println(name)

        val l = name!!.length // !! 抛出空指针异常

        val listWithNull: List<String?> = listOf("kotlin", null)
        listWithNull.filterNotNull()
        listWithNull.filter { it != null }
                .forEach { println(it) }
        for (item in listWithNull) {
            item?.let { println(it) }
        }

        val len: Int = if (otherName != null) otherName!!.length else -1
        val len2 = otherName?.length ?: -1 // ?:   otherName!=null -> .length || -1


    }

    fun getScores(value: Int): Int = 2 * value

    private fun testObject() {
        val person = Person()
        person.lastName = "wang"
        println("lastName:${person.lastName}")

        person.no = 9
        println("no:${person.no}")

        person.no = 20
        println("no:${person.no}")

    }

    class Person {
        var name: String? = null
        var url: String? = null
        var lastName: String = "xiao"
            get() = field.toUpperCase()
        var no: Int = 100
            get() = field - 2
            set(value) {
                field = if (value < 10) value else -1
            }
    }

    fun testUtils() {
        val score = Utils.getScore(5)
    }

    // 构造器
    class Utils private constructor() {
        // Kotlin 使用伴生对象关键字 companion 来定义静态工厂方法
        companion object {
            fun getScore(value: Int): Int {
                return 2 * value
            }
            /*public static int getScore(int value){
                return 2 * value;
            }*/
        }

        // 次构造函数
        constructor(name: String) : this() {
            println("他的名字：$name")


        }
    }

    // 单例：同时创建了实例
    object singletons {
        fun action() {
            println(this.hashCode())
        }
    }

    fun testSingle() {
        val instance = singletons
        val instance2 = singletons
    }

    // 组合优于继承
    class CountintSetBy(val countingSet: MutableCollection<String>) : MutableCollection<String> by countingSet {
        var count = 0
        override fun add(element: String): Boolean {
            count++
            return countingSet.add(element)
        }

        override fun addAll(elements: Collection<String>): Boolean {
            count += elements.size
            return countingSet.addAll(elements)
        }
//        fun contains(element: String) {
//            countingSet.contains(element)
//        }
//
//        fun add(element: String): Boolean {
//            count++
//            return countingSet.add(element)
//        }
    }


    data class Developer(val name: String, val age: Int)

    private fun Int.triple(): Int {
        return this * 3
    }

    enum class Direction constructor(direction: Int) {
        NORTH(1), SOUTH(2), WEST(3), EAST(4);

        var direction: Int = 0
            private set

        init {
            this.direction = direction
        }
    }

    fun foo(person: Person): String? {
        val name = person.name ?: return null
        val url = person.url ?: throw IllegalArgumentException("name")
        return ""
    }

    // 抽象类
    open class Base {
        open fun f() {}
    }

    abstract class Derived : Base() {
        abstract override fun f()
    }

    class Outer {
        private val bar: Int = 1
        var v = "成员属性"

        inner class Inner {
            fun foo() = bar
            fun innerTest() {
                var o = this@Outer
                println(o.v)
            }
        }
    }

    interface TestInterface {
        fun test()
    }

    class Test {
        fun setInterface(test: TestInterface) {
            test.test()
        }
    }

    fun testInterface() {
        var test = Test()
        test.setInterface(object : TestInterface {
            override fun test() {

            }
        })
    }

    /**
     * 类修饰符：
     * abstract 抽象类
     * final 类不可继承，默认属性
     * open 类可继承，类默认是final的
     * enum 枚举类
     * annotation 注解类
     */
    /**
     * 访问权限修饰符：
     * private 进在同一个文件中可见
     * protected 同一个文件中或子类可见
     * public 多有调用的地方都可见
     * internal 同一个模块可见
     * */

    /**
     * 空安全 - 消除来自代码空引用的危险
     * ? 对象可为空 || 对象不为空时
     * ?: 否则，左侧表达式非空，则返回左侧表达式，否则返回右侧表达式
     * !! 该值为空，报空指针异常
     */
    fun testNullSafe() {
        var a: String = "abc"
        var b: String? = null
        var len = b?.length ?: -1
        // len = if(b == null) -1 else b.length
        println("字符串长度：${b?.length}")
        val listWithNull: List<String?> = listOf("kotlin", null)
        val intList : List<String> = listWithNull.filterNotNull()
        for (item in listWithNull) {
            item?.let { println(item) }
        }

        len = b!!.length

        // 安全类型转换
        val i: Int? = b as? Int // 转换不成功，返回null
    }

    /**
     * 类与继承
     * 一个类可以有一个主构造函数一级一个或者多个次构造函数, constructor关键字可以省略
     *
     * var 可变 || val 只读
     */
    class Announce public constructor(name: String) {
        val customerKey = name.toUpperCase() //主构造的参数 可以在 类体内声明的属性初始化器 中使用

        init {
            // 主构造函数不能包含任何代码，初始化的代码可以放到以init关键字作为前缀的初始化块中
            // 初始化块按照它们出现在类体中的顺序执行
            println("First initializer block that prints $name") // 主构造的参数可以在初始化块中使用
        }


    }

    fun testKotlin(x: Int): Int {
        var age: Int = 9
        var age2: Int
        var age3 = 3

        val a: Int = 5

        x as Short // 类型转换
        var b = x is Int // 类型检测


        var c = if (age > age3) age else age3
        when (age) {
            0 -> {
            }
            2, 3 -> {
            }
            else -> {
            }
        }
        for (i in 1..4) {

        }
        var j = 5
        while (j in 1..4) {

        }

        if (j in 1..5) {
            // [1,5]
        }
        if (j in 1 until 5) {
            // [1,5)
        }
        for (j in 1 until 5) {
            println(j) // 1,2,3,4
        }
        for (j in 5 downTo 1) {

        }
        for (j in 1..5 step 2) {
            // 1,3,5
        }
        for (j in 5 downTo 1 step 2 ){
            // 5,3,1
        }
        // === 引用相同
        // equal == 结构相同

        var m = null
        m!!
        var n :String? = "b" // n可为null
        n = null



        return x * 2
    }

    fun add(a: Int, b: Int = 3): Int = a + b // 默认参数 简写函数

    enum class Color(rgb: Int) {
        RED(0xFF0000), GREEN(0x00FF00), BLUE(0x0000FF)
    }

    enum class Type {
        VERIFY, FRAME
    }

}