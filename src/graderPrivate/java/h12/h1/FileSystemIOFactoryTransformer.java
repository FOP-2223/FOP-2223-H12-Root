package h12.h1;

import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

public class FileSystemIOFactoryTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return "FileSystemIOFactory";
    }

    @Override
    public void transform(final ClassReader reader, final ClassWriter writer) {
        if ("h12/ioFactory/FileSystemIOFactory".equals(reader.getClassName())) {
            reader.accept(new CV(Opcodes.ASM9, writer), 0);
        } else {
            reader.accept(writer, 0);
        }
    }

    private static class CV extends ClassVisitor {

        public CV(final int api, final ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(final int access, final String name, final String descriptor,
                                         final String signature, final String[] exceptions) {
            if ("createReader".equals(name)) {
                return new MV("Reader", api, super.visitMethod(access, name, descriptor, signature, exceptions));
            } else if ("createWriter".equals(name)) {
                return new MV("Writer", api, super.visitMethod(access, name, descriptor, signature, exceptions));
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        private static class MV extends MethodVisitor {

            private final String visitName;

            public MV(final String visitName, final int api, final MethodVisitor methodVisitor) {
                super(api, methodVisitor);
                this.visitName = visitName;
            }

            @Override
            public void visitTypeInsn(final int opcode, final String type) {
                if (opcode == Opcodes.NEW) {
                    if (("java/io/File" + visitName).equals(type)) {
                        super.visitTypeInsn(opcode, "h12/h1/TutorFile" + visitName);
                    } else if (("java/io/Buffered" + visitName).equals(type)) {
                        super.visitTypeInsn(opcode, "h12/h1/TutorBuffered" + visitName);
                    } else {
                        super.visitTypeInsn(opcode, type);
                    }
                } else {
                    super.visitTypeInsn(opcode, type);
                }
            }

            @Override
            public void visitMethodInsn(final int opcode, final String owner, final String name,
                                        final String descriptor, final boolean isInterface) {
                if (opcode == Opcodes.INVOKESPECIAL && "<init>".equals(name)) {
                    if (("java/io/File" + visitName).equals(owner)) {
                        super.visitMethodInsn(
                            Opcodes.INVOKESPECIAL,
                            "h12/h1/TutorFile" + visitName,
                            name,
                            descriptor,
                            false
                        );
                    } else if (("java/io/Buffered" + visitName).equals(owner)) {
                        super.visitMethodInsn(
                            Opcodes.INVOKESPECIAL,
                            "h12/h1/TutorBuffered" + visitName,
                            name,
                            descriptor,
                            false
                        );
                    } else {
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                } else {
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                }
            }
        }
    }

}
