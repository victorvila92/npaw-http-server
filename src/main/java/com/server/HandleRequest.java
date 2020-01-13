package com.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class HandleRequest extends HttpServlet {

    private static Random r = new Random();
    private static final Integer NUM_CHARS = 32;
    private static final Logger LOGGER = Logger.getLogger(HandleRequest.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().println(getRandomHexString());
        }catch (IOException ex){
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    private String getRandomHexString(){
        StringBuilder sb = new StringBuilder();
        while (sb.length() < NUM_CHARS){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, NUM_CHARS);
    }

    public static boolean isHexValid(String value){
        return value.matches("-?[0-9a-fA-F]+");
    }
}