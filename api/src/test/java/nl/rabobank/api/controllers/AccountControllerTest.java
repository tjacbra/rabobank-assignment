package nl.rabobank.api.controllers;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.api.converters.PowerOfAttorneyConverter;
import nl.rabobank.api.dto.PowerOfAttorneyDto;
import nl.rabobank.api.services.PowerOfAttorneyService;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PowerOfAttorneyConverter powerOfAttorneyConverter;

    @MockBean
    private PowerOfAttorneyService powerOfAttorneyService;

    @Test
    public void whenGivenANewPowerOfAttorney_thenCreatePowerOfAttorney_shouldCreateAPowerOfAttorney() throws Exception {
        var powerOfAttorneyArgumentCaptor = ArgumentCaptor.forClass(PowerOfAttorney.class);

        when(powerOfAttorneyConverter.convertDtoToDomain(any(PowerOfAttorneyDto.class))).thenCallRealMethod();
        when(powerOfAttorneyService.createPowerOfAttorney(powerOfAttorneyArgumentCaptor.capture())).thenReturn(true);

        mockMvc.perform(post("/api/power_of_attorneys")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "\"granteeName\": \"grantee\",\n" +
                        "\"grantorName\": \"grantor\",\n" +
                        "\"accountType\": \"SAVINGS_ACCOUNT\",\n" +
                        "\"accountNumber\": \"accountNumber\",\n" +
                        "\"authorization\": \"WRITE\"\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "PowerOfAttorneyDto(" +
                                "granteeName=grantee, " +
                                "grantorName=grantor, " +
                                "accountType=SAVINGS_ACCOUNT, " +
                                "accountNumber=accountNumber, " +
                                "authorization=WRITE) " +
                                "created successfully"));

        assertNotNull(powerOfAttorneyArgumentCaptor.getValue());
    }

    @Test
    public void whenGivenAnExistingPowerOfAttorney_thenCreatePowerOfAttorney_shouldCreateAPowerOfAttorney() throws Exception {
        var powerOfAttorneyArgumentCaptor = ArgumentCaptor.forClass(PowerOfAttorney.class);

        when(powerOfAttorneyConverter.convertDtoToDomain(any(PowerOfAttorneyDto.class))).thenCallRealMethod();
        when(powerOfAttorneyService.createPowerOfAttorney(powerOfAttorneyArgumentCaptor.capture())).thenReturn(false);

        mockMvc.perform(post("/api/power_of_attorneys")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "\"granteeName\": \"grantee\",\n" +
                        "\"grantorName\": \"grantor\",\n" +
                        "\"accountType\": \"SAVINGS_ACCOUNT\",\n" +
                        "\"accountNumber\": \"accountNumber\",\n" +
                        "\"authorization\": \"WRITE\"\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "PowerOfAttorneyDto(" +
                                "granteeName=grantee, " +
                                "grantorName=grantor, " +
                                "accountType=SAVINGS_ACCOUNT, " +
                                "accountNumber=accountNumber, " +
                                "authorization=WRITE) " +
                                "already granted"));

        assertNotNull(powerOfAttorneyArgumentCaptor.getValue());
    }

    @Test
    public void whenGivenAnIncompletePowerOfAttorney_thenCreatePowerOfAttorney_shouldReturnABadRequest() throws Exception {
        mockMvc.perform(post("/api/power_of_attorneys")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "\"granteeName\": \"grantee\",\n" +
                        "\"grantorName\": \"grantor\",\n" +
                        "\"accountNumber\": \"accountNumber\",\n" +
                        "\"authorization\": \"WRITE\"\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGivenNoPowerOfAttorney_thenCreatePowerOfAttorney_shouldReturnABadRequest() throws Exception {
        mockMvc.perform(post("/api/power_of_attorneys")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGivenInvalidContentType_thenCreatePowerOfAttorney_shouldReturnUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/api/power_of_attorneys")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content("<xml/>")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void whenGivenAGranteeName_thenGetAccounts_shouldReturnAccountsForTheGrantee() throws Exception {
        var granteeArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(powerOfAttorneyConverter.convertDomainToAccountDto(any(PowerOfAttorney.class))).thenCallRealMethod();
        when(powerOfAttorneyService.findAllPowerOfAttorneyByGranteeName(granteeArgumentCaptor.capture())).
                thenReturn(List.of(PowerOfAttorney
                        .builder()
                        .grantorName("grantor")
                        .granteeName("grantee")
                        .authorization(Authorization.WRITE)
                        .account(new PaymentAccount("accountNumber", "", 0.0))
                        .build()));

        mockMvc.perform(get("/api/accounts/grantee/grantee")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(
                                "[" +
                                        "{\"grantorName\":\"grantor\"," +
                                        "\"accountType\":\"PAYMENT_ACCOUNT\"," +
                                        "\"accountNumber\":\"accountNumber\"," +
                                        "\"authorization\":" +
                                        "\"WRITE\"}]"
                        ));

        assertEquals("grantee", granteeArgumentCaptor.getValue());
    }

    @Test
    public void whenGivenNoGranteeName_thenGetAccounts_shouldReturn() throws Exception {
        mockMvc.perform(get("/api/accounts/grantee/")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

}
