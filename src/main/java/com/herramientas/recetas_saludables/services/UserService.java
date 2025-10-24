package com.herramientas.recetas_saludables.services;

import com.herramientas.recetas_saludables.model.User;
import com.herramientas.recetas_saludables.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public List<User> listarTodos(){
        return userRepository.findAll();
    }

    public void guardar(User user){
        userRepository.save(user);
    }

    public User obtenerPorId(Long id){
        return userRepository.findById(id).orElse(null);
    }
    public User obtenerPorCorreo(String correo){

        return userRepository.findByCorreo(correo).orElse(null);
    }

    public void eliminar(Long id){
        userRepository.deleteById(id);
    }

}
