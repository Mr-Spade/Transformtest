package com.example.plugin;

import java.io.*;
import java.util.*;

import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.QualifiedContent.ContentType;
import com.android.build.api.transform.QualifiedContent.Scope;

import org.gradle.api.Project;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.internal.impldep.org.eclipse.jgit.gitrepo.RepoProject.CopyFile;

import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

public class MyTransform extends Transform{
        public static void fileCopy(File file1,File file2){
            File[] files1 = file1.listFiles();
            for (int i = 0; i < files1.length; i++) {
                if (files1[i].isDirectory()){
                    String s = files1[i].getName();
                    File file = new File(file2.getPath()+"/"+s);
                    file.mkdir();
                    fileCopy(files1[i],file);
                }else if (files1[i].isFile()){
                    String s = files1[i].getName();
                    BufferedReader br = null;
                    BufferedWriter bw = null;
                    try {
                        
                    ByASM.mian(new File(files1[i].getPath()+""), new File(file2.getPath()+"/"+s));
                    //     FileInputStream fin=new FileInputStream(files1[i].getPath()+"");
                    // // System.out.println("wuhuc!");
                    // byte[] buf=new byte[fin.available()];
                    // // System.out.println("wuhuc!");
                    // fin.read(buf);fin.close();
                    // // System.out.println("wuhuc!");
                    // FileOutputStream fout=new FileOutputStream(file2.getPath()+"/"+s);
                    // // System.out.println("wuhuc!");
                    // fout.write(buf);fout.close();
                        // br = new BufferedReader(new InputStreamReader(new FileInputStream(files1[i].getPath()+"")));
                        // bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2.getPath()+"/"+s)));
                        // String t;
                        // while(true){
                        //     if ((t=br.readLine())!=null){
                        //         bw.write(t);
                        //     }else{
                        //         break;
                        //     }
                        // }
    
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (br!=null){
                            try {
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }if (bw!=null){
                            try {
                                bw.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    private Project project;

    MyTransform(Project project){
        this.project=project;
    }

    @Override
    public String getName() {
        System.out.println("getName!");
        return "shadew";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        System.out.println("getInputTypes!");
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        System.out.println("getInputTypes!");
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        System.out.println("getInputTypes!");
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        // mian();
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        inputs.forEach(transformInput -> {
            transformInput.getJarInputs().forEach(jarInput -> {
                File dest = outputProvider.getContentLocation(jarInput.getName(), jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);
                try{
                    System.out.println("wuhua!");
                    String a=jarInput.getFile().getPath(),b=dest.getPath();
                    System.out.println(a);
                    System.out.println(b);
                    // System.out.println("wuhuc!");
                    FileInputStream fin=new FileInputStream(jarInput.getFile());
                    // System.out.println("wuhuc!");
                    byte[] buf=new byte[fin.available()];
                    // System.out.println("wuhuc!");
                    fin.read(buf);fin.close();
                    // System.out.println("wuhuc!");
                    FileOutputStream fout=new FileOutputStream(dest);
                    // System.out.println("wuhuc!");
                    fout.write(buf);fout.close();
                    // FileUtils.copyFile(new File("F:\\android_development\\TransformTest1\\app\\build\\intermediates\\transforms\\shadew\\debug\\42.jar"), new File("F:\\android_development\\TransformTest1\\app\\build\\intermediates\\transforms\\shadew2\\debug\\42.jar"));
                    System.out.println("wuhua!");
                } catch(IOException e){

                }
            });

            transformInput.getDirectoryInputs().forEach(directoryInput -> {
                File dest = outputProvider.getContentLocation(directoryInput.getName(), directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
                dest.mkdir();
                fileCopy(directoryInput.getFile(), dest);
                // try {
                    // File [] fl=directoryInput.getFile().;
                    // for(File f:fl){
                        System.out.println("wuhub!");
                        System.out.println(directoryInput.getFile().getPath());
                        System.out.println(dest.getPath());
                        System.out.println("wuhub!");
                    // }
                // } catch (IOException e) {
                //     System.out.println("wuhu!");
                //     e.printStackTrace();
                // }
            });
        });
    }

    // public static void mian() throws FileNotFoundException {  
    //     ClassWriter cw = new ClassWriter(Opcodes.ASM5);  
    //     ClassNode cn = gen();  
    //     cn.accept(cw);  
    //     File file = new File("ChildClass.class");  
    //     FileOutputStream fout = new FileOutputStream(file);  
    //     try {  
    //         fout.write(cw.toByteArray());  
    //         fout.close();  
    //     } catch (IOException e) {  
    //         e.printStackTrace();  
    //     }  
  
    // }  
  
    // private static ClassNode gen() {  
    //     ClassNode classNode = new ClassNode();  
    //     classNode.version = Opcodes.V1_8; 
    //     classNode.access = Opcodes.ACC_PUBLIC ;//+ Opcodes.ACC_ABSTRACT;  
    //     classNode.name = "asm/core/ChildClass";  
    //     classNode.superName = "java/lang/Object";  
    //     // classNode.interfaces.add("java/io/IOException");  
    //     classNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "zero", "I",  
    //             null, 233));  
    //     classNode.methods.add(new MethodNode(Opcodes.ACC_PUBLIC /*+ Opcodes.ACC_ABSTRACT*/, "main",  
    //             "(Ljava/lang/String;)I", null, null));  
    //     return classNode;  
    // }  
}
