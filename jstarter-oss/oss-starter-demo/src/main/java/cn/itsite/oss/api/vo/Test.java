package cn.itsite.oss.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2018/11/25 0025 20:13
 * @description:
 */
@Data
public class Test {

    @NotNull(message = "account{NotNull}")
    private String account;

    @Min(value = 0, message = "age{NOT_LESS_THAN}{value}")
    @Max(value = 100, message = "age{NOT_GREATER_THAN}{value}")
    private Integer age = 0;

    private String gender;

    @JsonIgnore
    @NotNull(message = "password{NotNull}")
    private String password;

    private Boolean rememberMe = false;
}
