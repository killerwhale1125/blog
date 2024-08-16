package react.blog.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @NotNull
    private String address;

    private String detail;

    public Address(String address, String detail) {
        this.address = address;
        this.detail = detail;
    }
}
