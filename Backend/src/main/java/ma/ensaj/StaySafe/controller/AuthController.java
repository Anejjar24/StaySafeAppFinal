package ma.ensaj.StaySafe.controller;


import ma.ensaj.StaySafe.entity.User;
import ma.ensaj.StaySafe.repository.UserRepository;
import ma.ensaj.StaySafe.security.JwtResponse;
import ma.ensaj.StaySafe.security.JwtService;
import ma.ensaj.StaySafe.security.LoginRequest;
import ma.ensaj.StaySafe.security.PasswordResetRequest;
import ma.ensaj.StaySafe.service.EmailService;
import ma.ensaj.StaySafe.service.UserDetailsServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Utilisateur enregistré avec succès");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Email non trouvé");
        }

        // Générer un mot de passe aléatoire
        String newPassword = generateRandomPassword();

        // Mettre à jour l'utilisateur avec le nouveau mot de passe
        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Envoyer l'email
        emailService.sendPasswordResetEmail(user.getEmail(), newPassword);

        return ResponseEntity.ok("Un nouveau mot de passe a été envoyé à votre email");
    }

    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser() {
        User user = userDetailsService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(user);
    }
//    @GetMapping("/user-info")
//    public ResponseEntity<?> getUserInfo(Principal principal) {
//        String email = principal.getName(); // Extraire l'email de l'utilisateur depuis le JWT
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
//
//        return ResponseEntity.ok(user); // Retourner l'objet utilisateur en JSON
//    }
//@GetMapping("/user-info")
//public ResponseEntity<?> getUserInfo() {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//    if (authentication == null || !authentication.isAuthenticated()) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié");
//    }
//
//    String email = authentication.getName(); // Récupération du nom d'utilisateur
//    System.out.println("Nom d'utilisateur extrait du contexte de sécurité : " + email);
//
//    User user = userRepository.findByEmail(email)
//            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
//
//    return ResponseEntity.ok(user);
//}

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Si pas de token JWT, on vérifie quand même si un header Authorization est présent
        String authHeader = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtService.extractUsername(token);

            if (email != null) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
                return ResponseEntity.ok(user);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide ou manquant");
    }


}
