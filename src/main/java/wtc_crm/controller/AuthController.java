package wtc_crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import wtc_crm.dto.LoginRequest;
import wtc_crm.model.User;
import wtc_crm.repository.UserRepository;
import wtc_crm.security.JwtService;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // ✅ REGISTRO
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        // verifica se já existe
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Usuário já existe");
        });

        // criptografa senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole("ROLE_CLIENT");
        }

        return userRepository.save(user);
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public HashMap<String, String> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(user.getEmail());

        HashMap<String, String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }
}
