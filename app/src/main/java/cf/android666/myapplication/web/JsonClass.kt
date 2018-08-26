package cf.android666.myapplication.web

/**
 * Created by jixiaoyong on 2018/8/25.
 * email:jixiaoyong1995@gmail.com
 */
class JsonClass(var arr: Array<Arr>) {

    data class Arr(var id: Int, var username: String, var age: Int, var email: String, var create_time: String)
}