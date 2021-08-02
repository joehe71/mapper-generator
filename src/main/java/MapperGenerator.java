import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
            if (method.getParameterCount() > 1) {
                throw new RuntimeException("mapper method param is only one , mapper class " + mapperClass.getName());
            }
            StringJoiner sj = new StringJoiner("\n");
            Class<?> parameterType = method.getParameterTypes()[0];
            Class<?> returnType = method.getReturnType();

            Map<String, ? extends Class<?>> parameterFieldMap = Arrays.stream(parameterType.getFields())
                    .collect(Collectors.toMap(Field::getName, Field::getType));

            Map<String, ? extends Class<?>> returnFieldMap = Arrays.stream(returnType.getFields())
                    .collect(Collectors.toMap(Field::getName, Field::getType));

            returnFieldMap.forEach((k, v) -> {
                Class<?> parameterField = parameterFieldMap.get(k);
                if (parameterField != null && parameterField == v) {
                    sj.add(SETTER_CODE.formatted(k));
                }
            });
            String methodBody = METHOD_DECLARE_CODE.formatted(returnType.getName(), method.getName(), parameterType.getName(), sj);
            cc.addMethod(CtMethod.make(methodBody, cc));
        }
        cc.writeFile();
        return (T) cc.toClass(mapperClass).getDeclaredConstructor().newInstance();
    }
}
