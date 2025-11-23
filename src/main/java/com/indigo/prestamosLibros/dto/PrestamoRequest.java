/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.dto;

/**
 *
 * @author ANDRES GUTIERREZ
 */
public record PrestamoRequest (
        String isbn,
        String identificacionUsuario,
        Integer tipoUsuario) {}
