package org.example;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Collection;
import java.util.Objects;

import static org.example.hashFunctions.fnv.FNV.*;
import static org.example.hashFunctions.murmur3.Murmur.hash32;

public class Bloomfilter <T> implements Serializable {

    public static final double LOG_2 = Math.log(2);
    public static final long UINT_MASK = 0xFFFFFFFFl;


    // the backbone of the data structure
    private BitSet bitArray;
    private int bitArraySize;

    private double numBitsOfElem; // the number of bits that are allocated for each element that is added to the filter.
    private int k;// number of hash functions.

    private int capacity; // the maximum number of elements that DS can have.
    private int size;// number of actual added elements.


    // there are multiple constructors provided for each useCase

    /**
    * Create a bloom filter of
    *  capacity n
    *  assign e bits for each element
    *  use K hash functions
    * */

    public Bloomfilter(int n,double e,int k){
        this.capacity=n;
        this.numBitsOfElem=e;
        this.k=k;
        this.bitArraySize=(int)Math.ceil(e * n);
        this.size=0;
        this.bitArray=new BitSet(bitArraySize);
    }

    /**
    * Create a bloom filter of
    * bit array Size of bitArraySize
    * capacity of n
    * */
    public Bloomfilter(int bitArraySize,int n){
        this(
               n,
            bitArraySize/(double)n,
              (int) Math.round((bitArraySize/(double)n)*LOG_2)
            );

    }

    /**
     *     * Create a bloom filter with
     *       desired false positive probability
     *        expected number of elements in the Bloom filter -capacity.
     */
    public Bloomfilter(double falsePositiveProbability, int n) {
        this(   n,
                Math.ceil(-(Math.log(falsePositiveProbability) / LOG_2)) / LOG_2, // c = k / ln(2)
                (int)Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2)))); // k = ceil(-log_2(false prob.))
    }

    /**
    *In case of deep copy of Bloom Filter
     */

    public Bloomfilter(int bitArraySize, int n, int actualNumberOfFilterElements, BitSet filterData) {
        this(bitArraySize, n);
        this.bitArray = filterData;
        this.size = actualNumberOfFilterElements;
    }








    /**
     * add element to Bloom Filter
     * */
   public void add(T element){
       if(element==null)
           return;
       try {
           add(element.toString().getBytes());
           this.size++;
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }
    /**
     * add String to Bloom Filter
     * */
    public void add(byte[] data) throws Exception {
        if(this.size==this.capacity)
            throw new Exception("Limit exceeded");
        int murmur=hash32(data,0);
        int FNV=fnv(data);


       for(int i=1;i<=this.k;i++){
           int cur=murmur+ (int)((long)i*FNV);
           if(cur <0)
               cur=~cur;
           this.bitArray.set(cur % this.bitArraySize);

       }
    }

    public void addAll (@NotNull Collection<T> elements){
        for(T element : elements){
            try {
                add(element);
            }
            catch (Exception e){
                e.getCause();
            }
        }
    }


    /**
     * takes element as Input
     * return true if the Bloom Filter contains that element
     * */
     public boolean contains( @NotNull T element){
        return contains(element.toString().getBytes());
     }
     public boolean contains(byte[] data){

         int murmur=hash32(data,0);
         int FNV=fnv(data);

         boolean result=true;
         for(int i=1;i<=this.k;i++){
             int cur=murmur+ (int)((long)i*FNV);
             if(cur <0)
                 cur=~cur;
            result&= this.bitArray.get(cur % this.bitArraySize);

         }
         return result;
     }


    /**
     * Getters
     * */

    /**
     * Calculate the probability of a false positive given the specified
     * number of inserted elements.
     */
    public double getFalsePositiveProbability(double numberOfElements) {
        // (1 - e^(-k * n / m)) ^ k
        return Math.pow((1 - Math.exp(-k * (double) numberOfElements
                / (double) bitArraySize)), k);

    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getK() {
        return k;
    }

    public double getNumBitsOfElem() {
        return numBitsOfElem;
    }
}
