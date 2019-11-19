package com.xs.lightpuzzle.demo.a_kotlin_demo

import android.graphics.Color
import android.os.Handler
import android.os.Parcelable
import android.view.View
import android.widget.Checkable
import androidx.core.os.bundleOf
import kotlinx.android.parcel.Parcelize
import android.content.SharedPreferences as SharedPreferences1

/**
 * Author: xs
 * Create on: 2019/10/31
 * Description: _
 */
class TestKotlin {
    fun testNullSafe() {
        var s: String? = "abc"
        s = null
        println(s)

        var s2: String = "abc2"

        var len = s2.length
        len = s?.length!! // == null 抛NPE
        len = s?.length ?: -1 // 左侧表达式非空，则返回左侧表达式，否则返回右侧表达式
        len = if (s == null) -1 else s.length // 三木表达式

        val a: Int? = s2 as? Int // 转型失败则返回null

        val listWithNull: List<String?> = listOf("kotlin", null)
        val intList: List<String> = listWithNull.filterNotNull() // 过滤非空元素
        for (item in listWithNull) {
            item?.let { println(item) }
        }
    }

    /**
     * 访问权限修饰符：
     * private 进在同一个文件中可见
     * protected 同一个文件中或子类可见
     * public 多有调用的地方都可见
     * internal 同一个模块可见
     * */
    var isVisible = true // 默认public
    private var isHidden = true
    internal var almostVisible = true

    class Person(name: String?, age: Int) {
        var name: String? = null // default getter and setter
        val isVIP = true // just getter

        var age = 5
            get() = field + 10

        var sign = ""
            set(value) {
                // value = name of the setter parameter
                // field = property's backing field
//                sex = if (value == 1) "女" else "男"
                field = value + "年轻没有失败"
            }
        // ?????????????????
        var sex = ""
            set(value) {
                // value = name of the setter parameter
                // field = property's backing field
//                sex = if (value == 1) "女" else "男"
                field = value + ""
            }
    }

    // -------------------- 第一周 -----------------------
    fun practiceFirstWeek() {
        // Elvis
        val person = Person(null, 8)
        val name: String = person.name ?: "unknown"

        // String
        val language = "Kotlin"
        val text = "$language has ${language.length} characters"

        // When
        val x = 6
        when (name) {
            "unknown" -> {
            }
            "abc" -> {
            }
        }
        val result = when (x) {
            in 1..3 -> {
                "1,2,3"
            }
            4, 5 -> {
                "4,5"
            }
            6 -> {
                "6"
            }
            else -> {
                "nothing"
            }
        }

        //循环，范围表达式与解构
        for (i in 1..100) {

        }
        for (i in 100 downTo 1) {

        }
        val list = listOf(Person("a", 1), Person("b", 2), Person("c", 3))
        val array = arrayOf("a", "b", "c")
        for (i in 1..array.size step 2) {

        }
        for ((index, element) in list.withIndex()) {

        }
        val map = mapOf(1 to "one", 2 to "two")
        for ((key, value) in map) {

        }

        // 解构申明
        val (left, top, right, bottom) = RectVO(0, 0, 100, 100)
        println("left: $left top: $top right: $right bottom: $bottom")

        data class User(val name: String, val id: Int)

        val u = User("miss", 1)
        println("${u.name} , ${u.id}")
        val (n, i) = u // 解构申明
        println("$n, $i")
        println("${u.component1()} , ${u.component2()}")
    }

    class RectVO(val l: Int, val t: Int, val r: Int, val b: Int) {
        operator fun component1(): Int {
            return l
        }

        operator fun component2(): Int {
            return t
        }

        operator fun component3(): Int {
            return r
        }

        operator fun component4(): Int {
            return b
        }
    }

    // -------------------- 第二周 -----------------------
    fun practiceSecondWeek() {
        // 简单的bundle
        val bundle = bundleOf(
                Pair("KEY_PRICE", 50.0),
                Pair("KEY_IS_FROZEN", false)
        )

    }

    // 序列化 Parcelable
    @Parcelize
    data class Demo2(val name: String, val occupation: Int) : Parcelable

    data class User(val name: String) // data class 默认实现equals(),hashCode(),toString(),copy()方法

    val oldUserList = listOf(User("xs"), User("gyn"))
    val newUserList = listOf(User("xs"), User("xxy"))

    // 默认参数
    class Bullet(
            private val bulletRadius: Float = 5.5f,
            private val color: Int = Color.WHITE
    )

    val bullet = Bullet()
    val bullet2 = Bullet(10.0f)
    val bullet3 = Bullet(color = Color.BLACK)

    fun generateBullet(): Bullet {
        return Bullet()
    }


    // -------------------- 第三周 -----------------------
    fun practiceThirdWeek() {

    }


    // -------------------- 标准函数库 -----------------------
    fun method() {
        // run
        val str = "a"
        val res = str.run {
            // this 指向 "a"; it 没有指向
            // 可以直接访问对象得到属性
            println(length)
            1 // 最后一行为返回值
        }

        // let
        val str2 = "a"
        val res2 = str2.let {
            // this 指向当前class
            // it 指向"a"
            println(it.length)
            "let" // 返回值
        }

        // with
        val person = Person("xs", 8)
        val res3 = with(person) {
            // this -> person
            // it -> 无
            println(name)
            println(age)
            "with" // 返回值
        }

        // apply
        val str4 = "a"
        val res4 = str4.apply {
            // this -> a , it -> none
            2 // 返回值
        }
    }

    // 密封类
//    sealed class NetworkResult
//    data class Success2(val res: String) : NetworkResult()
//    data class Failure2(val error:String) : NetworkResult()
//    fun networkTest(res: NetworkResult){
//        when(res){
//            is Success
//        }
//    }

    // 扩展 eg：防止重复点击
    // 扩展点击事件属性(重复点击时长)
    var <T : View> T.lastClickTime: Long
        set(value) = setTag(1766613352, value)
        get() = getTag(1766613352) as? Long ?: 0
    // 重复点击事件绑定
    inline fun <T : View> T.singleClick(time: Long = 800, crossinline block: (T) -> Unit) {
        setOnClickListener {
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - lastClickTime > time || this is Checkable) {
                lastClickTime = currentTimeMillis
                block(this)
            }
        }
    }



}