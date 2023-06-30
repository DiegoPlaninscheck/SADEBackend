package br.weg.sade.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UtilFunctions {

    public static String[] getPropriedadesNulas (Object fonte) {
        BeanWrapper src = new BeanWrapperImpl(fonte);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static ArrayList<Integer> transformStringArray(String lista){
        StringBuilder sb = new StringBuilder(lista);
        ArrayList<Integer> listaNumeros = new ArrayList<>();

        sb.deleteCharAt(lista.length() - 1);
        sb.deleteCharAt(0);

        lista = sb.toString();
        lista = lista.replaceAll(",", " ");

        String[] s = lista.split("\\s+");

        for(int index = 0 ; index<s.length ; index++){
            listaNumeros.add(Integer.parseInt(s[index]));
        }

        return listaNumeros;
    }
}
