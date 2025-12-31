package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  private String studentUuid;

  @NotBlank(message = "入力必須項目に空欄があります。")
  private String name;

  @NotBlank(message = "入力必須項目に空欄があります。")
  private String furiganaName;

  private String nickname;

  @NotBlank(message = "入力必須項目に空欄があります。")
  @Email
  private String email;

  @NotBlank(message = "入力必須項目に空欄があります。")
  private String address;

  @Min(0)
  @Max(150)
  private Integer age;

  private String gender;

  private String remark;

  private boolean isDeleted;
}
