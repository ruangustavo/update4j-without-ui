## POC de Atualização Automática com Update4j

### Visão geral dos diretórios

1. `build`: Contém arquivos de configuração do Update4j e dependências da aplicação de negócio (
   imagens, bibliotecas, etc.)
2. `business`: Aplicação de negócio
3. `bootstrap`: Aplicação de bootstrap (responsável por baixar a versão mais recente da aplicação de
   negócio e executá-la)

### Como criar os arquivos de configuração?

1. Compile `business` e `bootstrap`:
   ```bash
   mvn package -pl business,bootstrap
   ```
   
   > O uso do `-pl` é para evitar que o `create-config` seja compilado também para o
   > diretório `build`. O `create-config` está na lista de diretórios excluidos do Maven mas, mesmo
   > assim, ele é compilado

2. Execute o main do `create-config` para gerar automaticamente os arquivos de configuração.

3. Faça o commit dos arquivos gerados (etapa importante para que o bootstrap consiga baixar a versão
   mais recente da aplicação de negócio remotamente).

> A ordem não é arbitrária, é importante seguir o passo-a-passo acima

### Observações

