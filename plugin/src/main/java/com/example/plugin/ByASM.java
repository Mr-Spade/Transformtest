package com.example.plugin;

// import java.io.*;
// import java.util.*;

// import org.objectweb.asm.*;
// import org.objectweb.asm.tree.*;

// public class ByASM {
//     public static void transform(File input,File output) throws IOException{
//         ClassReader cr=new ClassReader(new FileInputStream(input));
//         ClassNode cn=new ClassNode();
//         ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
//         cr.accept(cn,0);
//         cn.accept(cw);
//         // System.out.println("minor: "+(cn.version>>16));
//         // System.out.println("major: "+(cn.version&((1<<16)-1)));
//         // FileOutputStream fout=new FileOutputStream(output);
//         // fout.write(cw.toByteArray());
//         // fout.close();
//         return;
//     }
// }
import org.objectweb.asm.*;  
import org.objectweb.asm.tree.*;
import java.io.*;
import java.util.*;
public class ByASM{
    public class test_call{
        static Object get_value(Type t){
            switch(t.getInternalName()){
                case "I":return 233;
                case "F":return (float)233.3;
                case "D":return 233.33;
                case "J":return (long)2333;
                case "java/lang/String":return "23333";
            }
            return -1;
        }
        static void run(ClassNode cn,MethodNode mn,MethodNode test){
            Type[] lt=Type.getArgumentTypes(mn.desc);
            test.instructions.add(new VarInsnNode(Opcodes.ALOAD,0));
            for(Type t:lt){
                test.instructions.add(new LdcInsnNode(get_value(t)));
            }
            test.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,cn.name,mn.name,mn.desc));
            if(Type.getReturnType(mn.desc)!=Type.VOID_TYPE){
                test.instructions.add(new InsnNode(Opcodes.POP));
            }
            return;
        }
    }
    static public void transform(ClassNode cn){  
        MethodNode test=new MethodNode(Opcodes.ACC_PUBLIC,"test","()V",null,null);
        for(MethodNode mn:cn.methods){
            System.out.println(mn.name);
            if((mn.access&Opcodes.ACC_NATIVE)!=0){
                Type[] lt=Type.getArgumentTypes(mn.desc);
                for(Type t:lt){
                    System.out.println(t.getInternalName());
                }
                test_call.run(cn,mn,test);
            }
        }
        test.instructions.add(new InsnNode(Opcodes.RETURN));
        cn.methods.add(test);
    }
    public static void mian(File input,File output) throws IOException{
        ClassReader cr=new ClassReader(new FileInputStream(input));
        ClassNode cn=new ClassNode();
        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
        cr.accept(cn,0);
        transform(cn);
        cn.accept(cw);
        FileOutputStream fout = new FileOutputStream(output);  
        try{
            fout.write(cw.toByteArray());
            fout.close();
        }
        catch(IOException e){  
            e.printStackTrace();
        }
    }
}
