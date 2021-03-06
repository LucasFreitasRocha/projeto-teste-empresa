package com.nativa.resource;

import java.net.URI;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nativa.dto.in.CadastroDTO;
import com.nativa.dto.in.SenhaDTO;
import com.nativa.dto.out.TokenDto;
import com.nativa.dto.out.UsuarioDTO;
import com.nativa.model.Usuario;
import com.nativa.service.TokenService;
import com.nativa.service.UsuarioService;

@RestController
@RequestMapping("/user")
public class UsuarioResource {
    @Autowired private UsuarioService service;
    @Autowired private AuthenticationManager authManager;
    @Autowired private TokenService tokenService;


    @GetMapping
    public ResponseEntity<?> index
            (@RequestParam(required = false) String email,
             @RequestParam(required = false) String name,
             Pageable paginacao){
       if( Objects.isNull(email) &&  Objects.isNull(name)){
           return ResponseEntity.ok(service.index(paginacao));
       }else if(!Objects.isNull(email)){
           return ResponseEntity.ok(service.findByEmail(email));
       }else{
           return ResponseEntity.ok(service.findByName(name));
       }

    }

    @PostMapping
    public ResponseEntity<TokenDto> createUsuario(@Valid @RequestBody CadastroDTO cadastroDTO){
        UsernamePasswordAuthenticationToken dadosLogin = cadastroDTO.converter();
        Usuario usuario = service.createUser(cadastroDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuario.getId()).toUri();
        try {
            Authentication authentication = authManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.created(uri).body(new TokenDto(token, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> showUser(@PathVariable String id) {
        return ResponseEntity.ok(service.show(id));
    }
    @PutMapping("/{id}")
    public  ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody CadastroDTO cadastroDTO){
        service.updateUser(id, cadastroDTO);
        return  ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        service.deleteUser(id);
        return  ResponseEntity.noContent().build();
    }

    @PatchMapping("/alterar-senha")
    public ResponseEntity<Void>
    alterarSenha(@Valid @RequestBody SenhaDTO senhaDTO,@RequestHeader("Authorization") String token )
    {
        service.alterarSenha(token,senhaDTO);
        return  ResponseEntity.noContent().build();
    }

}
