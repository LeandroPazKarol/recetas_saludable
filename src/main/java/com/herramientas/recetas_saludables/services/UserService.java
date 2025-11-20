package com.herramientas.recetas_saludables.services;

import com.herramientas.recetas_saludables.model.User;
import com.herramientas.recetas_saludables.repository.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> listarTodos(){
        return userRepository.findAll();
    }

    public void guardar(@NonNull User user){
        userRepository.save(user);
    }

    @Nullable
    public User obtenerPorId(@NonNull Long id){
        return userRepository.findById(id).orElse(null);
    }
    @Nullable
    public User obtenerPorCorreo(@NonNull String correo){

        return userRepository.findByCorreo(correo).orElse(null);
    }

    public void eliminar(@NonNull Long id){
        userRepository.deleteById(id);
    }

}