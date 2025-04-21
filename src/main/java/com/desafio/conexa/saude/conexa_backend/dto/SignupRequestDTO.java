package com.desafio.conexa.saude.conexa_backend.dto;

import com.desafio.conexa.saude.conexa_backend.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record SignupRequestDTO(

        @Email(message = "Email inválido")
        String email,

        @Pattern(regexp = "^.{6,}$", message = "A senha deve conter no mínimo 7 caracteres.")
        String password,

        @Pattern(regexp = "^.{6,}$", message = "A confirmação da senha deve conter no mínimo 7 caracteres.")
        String confirmationPassword,

        @NotBlank(message = "Especialidade médica é obrigatório")
        String medicalSpecialty,

        @CPF
        String cpfNumber,

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "A data deve estar no formato yyyy-MM-dd")
        String dateBirth,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "Verifique o número de telefone, deve conter entre 10 e 11 dígitos")
        String phoneNumber,
        Role role
) {
}
