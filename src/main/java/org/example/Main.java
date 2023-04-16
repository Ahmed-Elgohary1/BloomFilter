package org.example;

import static org.example.hashFunctions.murmur3.Murmur.hash32;
import static org.example.hashFunctions.murmur3.Util.display;

public class Main {
    public static void main(String[] args) {

        Bloomfilter<String> b=new Bloomfilter<>(0.008,5);

        try {
           b.add("Ahmed Elgohary");
           // System.out.println("Hash from murmur: " + display(hash32("hello world",0)) );
       }
       catch (Exception e){
          e.printStackTrace();
       }
        System.out.println(b.contains("Ahmed"));
        System.out.println(b.getCapacity());

    }
}