/**
 * Created by hnvfh on 2016/12/5.
 */
public class ErrorEnum {
    public static final int 请求方式错误toGet = 100;
    public static final String Code_100 = "请求方式错误,请用Get方式请求";
    public static final int 请求方式错误toPost = 101;
    public static final String Code_101 = "请求方式错误,请用Post方式请求";

    public static final int 请求成功 = 200;
    public static final String Code_200 = "请求成功";

    public static final int Json解析异常 = 201;
    public static final String Code_201 = "Json解析异常";

    public static final int 数据库异常 = 202;
    public static final String Code_202 = "数据库异常";

    public static final int 数据库查询异常 = 203;
    public static final String Code_203 = "数据库查询异常";

    public static final int 数据库写入异常 = 204;
    public static final String Code_204 = "数据库写入异常";


    public static final int 参数未找到 = 205;
    public static final String Code_205 = "参数未找到";

    public static final int 请求失败 = 206;
    public static final String Code_206 = "请求失败";

    public static final int 参数错误 = 207;
    public static final String Code_207 = "参数错误";

    public static final int 参数log未找到 = 208;
    public static final String Code_208 = "参数log未找到";

    public static final int GZIP数据未找到 = 209;
    public static final String Code_209 = "GZIP数据未找到";

    public static final int 上传失败 = 301;
    public static final String Code_301 = "没有上传数据或者未识别信息";

    public static final int 上传失败_中断 = 302;
    public static final String Code_302 = "上传过程中网络异常，请重试";

    public static final int 文件未找到 = 303;
    public static final String Code_303 = "文件未找到";

    public static final int 文件读取失败 = 304;
    public static final String Code_304 = "文件读取失败";
}
