package pokeradar.asm;

import java.util.Iterator;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.Opcodes;

public class UnlimitedCharactersTransformer
        implements IClassTransformer
{
    public static String gui = "com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiTextFieldTransparent";

    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if (transformedName.equals(gui))
        {
            System.out.println("> Pokeradar UnlimitedCharactersTransformer");
            bytes = transformClass(transformedName, bytes);
        }
        return bytes;
    }

    private byte[] transformClass(String name, byte[] bytes)
    {
        System.out.println("Using class " + name);
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        Iterator it = classNode.methods.iterator();
        while (it.hasNext())
        {
            MethodNode writer = (MethodNode)it.next();
            transformMethod(writer);
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private void transformMethod(MethodNode method)
    {
        for (int i = 0; i < method.instructions.size(); i++)
        {
            AbstractInsnNode ins = method.instructions.get(i);
            if (ins.getOpcode() == Opcodes.BIPUSH)
            {
                IntInsnNode node = (IntInsnNode)ins;
                if (node.operand == 11)
                {
                    node.setOpcode(Opcodes.SIPUSH);
                    node.operand = 50;
                }
            }
        }
    }
}

