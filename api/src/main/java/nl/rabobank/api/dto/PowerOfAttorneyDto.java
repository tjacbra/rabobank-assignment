package nl.rabobank.api.dto;

import lombok.*;
import nl.rabobank.authorizations.Authorization;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PowerOfAttorneyDto {

    @NotNull
    @NonNull
    private String granteeName;
    @NotNull
    @NonNull
    private String grantorName;
    @NotNull
    @NonNull
    private AccountTypeDto accountType;
    @NotNull
    @NonNull
    private String accountNumber;
    @NotNull
    @NonNull
    private Authorization authorization;
}
