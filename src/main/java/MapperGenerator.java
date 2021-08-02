import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.StringJoiner;

/**
 * @author joe
 */
public class MapperGenerator {
    private static final String METHOD_DECLARE_CODE = """
                public %1$s %2$s(%3$s param) {
                    %1$s result = new %1$s();
                    %4$s
                    return result;
                }
            """;
    private static final String SETTER_CODE = "result.%1$s = param.%1$s;";

    public static <T> T generate(Class<T> mapperClass) throws Exception {
        if (!mapperClass.isInterface()) {
            throw new RuntimeException("mapper must a interface");
        }
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(mapperClass.getName() + "Impl");
        cc.addInterface(pool.get(mapperClass.getName()));
        for (Method method : mapperClass.getMethods()) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length > 1) {
                throw new RuntimeException("mapper method param is only one , mapper class " + mapperClass.getName());
            }
            StringJoiner sj = new StringJoiner("\n");
            Class<?> parameterType = parameterTypes[0];
            for (Field field : parameterType.getFields()) {
                sj.add(SETTER_CODE.formatted(field.getName()));
            }
            String methodBody = METHOD_DECLARE_CODE.formatted(method.getReturnType().getName(), method.getName(), parameterType.getName(), sj);
            cc.addMethod(CtMethod.make(methodBody, cc));
        }
        cc.writeFile();
        return (T) cc.toClass(mapperClass).getDeclaredConstructor().newInstance();
    }
}
