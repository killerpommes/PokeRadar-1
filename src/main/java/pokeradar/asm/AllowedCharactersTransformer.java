package pokeradar.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import net.minecraft.util.ChatAllowedCharacters;

import java.util.Iterator;

public class AllowedCharactersTransformer implements IClassTransformer
{
    @Override
    public byte[] transform( String name, String transformedName, byte[] bytes )
    {
        if ( transformedName.equals( "net.minecraft.util.ChatAllowedCharacters" ) )
        {
            System.out.println( "> Pokeradar AllowedCharactersTransformer" );
            bytes = transformClass( transformedName, bytes );
        }

        return bytes;
    }

    private byte[] transformClass( String name, byte[] bytes )
    {
        System.out.println( "Using class " + name );

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader( bytes );
        classReader.accept( classNode, 0 );

        Iterator< MethodNode > it = classNode.methods.iterator();
        while ( it.hasNext() )
        {
            MethodNode method = it.next();
            transformMethod( method );
        }

        ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
        classNode.accept( writer );
        return writer.toByteArray();
    }

    private void transformMethod( MethodNode method )
    {
        //System.out.println( "Using method " + method.name );

        // getAllowedCharacters
        /*boolean foundClose = false;
        for ( int i = 0; i < method.instructions.size(); ++i )
        {
            AbstractInsnNode ins = method.instructions.get( i );
            if ( ins.getOpcode() == Opcodes.INVOKEVIRTUAL )
            {
                MethodInsnNode node = ( MethodInsnNode ) ins;
                if ( node.owner.equals( "java/io/BufferedReader" ) && node.name.equals( "close" ) )
                {
                    foundClose = true;
                }
            }
            else if ( ins.getOpcode() == Opcodes.ALOAD && foundClose )
            {
                VarInsnNode node = ( VarInsnNode ) ins;
                if ( node.var == 0 )
                {
                    injectFormatSymbol( method, i );
                    foundClose = false;
                }
            }
        }*/

        // isAllowedCharacter
        for ( int i = 0; i < method.instructions.size(); ++i )
        {
            AbstractInsnNode ins = method.instructions.get( i );
            if ( ins.getOpcode() == Opcodes.SIPUSH )
            {
                // Remove limitation of character 167
                IntInsnNode node = ( IntInsnNode ) ins;
                if ( node.operand == 167 )
                {
                    System.out.println( "Found value 167, assuming it to be the formatting symbol." );
                    node.operand = 0;
                }
            }
        }
    }

    private void injectFormatSymbol( MethodNode method, int index )
    {
        System.out.println( "Injecting at index " + index );

        InsnList list = new InsnList();
        list.add( new MethodInsnNode( Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false ) );
        list.add( new FieldInsnNode( Opcodes.GETSTATIC, "pokeradar/inject/ColorData", "formatSymbol", "Ljava/lang/String;" ) );
        list.add( new MethodInsnNode( Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false ) );
        list.add( new MethodInsnNode( Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false ) );
        list.add( new VarInsnNode( Opcodes.ASTORE, 0 ) );

        method.instructions.insertBefore( method.instructions.get( index ), list );
    }
}