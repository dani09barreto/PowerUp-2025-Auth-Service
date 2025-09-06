package co.com.pragma.bootcamp.auth.model.token;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;


public record Token (
        String token
){
}
