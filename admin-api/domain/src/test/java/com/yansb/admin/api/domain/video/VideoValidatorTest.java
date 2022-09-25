package com.yansb.admin.api.domain.video;

import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.DomainException;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoValidatorTest {
  @Test
  public void givenNullTitle_whenCallsValidate_shouldReceiveError() {
    // given
    final String expectedTitle = null;
    final var expectedDescription = """
        This course is designed to help you prepare for system design interviews.
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'title' should not be null";
    final var actualVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );
    final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

    // when
    final var actualErrors = Assertions.assertThrows(
        DomainException.class,
        () -> validator.validate()
    );

    // then
    Assertions.assertEquals(expectedErrorCount, actualErrors.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualErrors.getErrors().get(0).message());
  }

  @Test
  public void givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
// given
    final var expectedTitle = "";
    final var expectedDescription = """
        This course is designed to help you prepare for system design interviews.
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'title' should not be empty";
    final var actualVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );
    final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

    // when
    final var actualErrors = Assertions.assertThrows(
        DomainException.class,
        () -> validator.validate()
    );

    // then
    Assertions.assertEquals(expectedErrorCount, actualErrors.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualErrors.getErrors().get(0).message());
  }

  @Test
  public void givenTitleWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
    // given
    final var expectedTitle = """
          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum arcu vitae elementum curabitur vitae nunc. 
          A iaculis at erat pellentesque adipiscing commodo elit at imperdiet. Id venenatis a condimentum vitae sapien pellentesque habitant morbi tristique. Mauris pellentesque pulvinar pellentesque habitant morbi. 
          Consectetur a erat nam at lectus urna duis convallis convallis. In fermentum posuere urna nec. Penatibus et magnis dis parturient montes nascetur ridiculus. Magnis dis parturient montes nascetur ridiculus mus. 
          Ultrices gravida dictum fusce ut placerat orci nulla. Orci eu lobortis elementum nibh tellus molestie nunc. Justo donec enim diam vulputate ut pharetra sit. Commodo nulla facilisi nullam vehicula ipsum a arcu.
        """;
    final var expectedDescription = """
        This course is designed to help you prepare for system design interviews.
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'title' must be between 1 and 255 characters";
    final var actualVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );
    final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

    // when
    final var actualErrors = Assertions.assertThrows(
        DomainException.class,
        () -> validator.validate()
    );

    // then
    Assertions.assertEquals(expectedErrorCount, actualErrors.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualErrors.getErrors().get(0).message());
  }

  @Test
  public void givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
    // given
    final var expectedTitle = "System Design Interview";
    final var expectedDescription = "";
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'description' should not be empty";
    final var actualVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );
    final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

    // when
    final var actualErrors = Assertions.assertThrows(
        DomainException.class,
        () -> validator.validate()
    );

    // then
    Assertions.assertEquals(expectedErrorCount, actualErrors.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualErrors.getErrors().get(0).message());
  }

  @Test
  public void givenDescriptionWithLengthGreatherThan4000_whenCallsValidate_shouldReceiveError() {
    // given
    final var expectedTitle = "System Design Interview";
    final var expectedDescription = """
        Nunca é demais lembrar o peso e o significado destes problemas, uma vez que o fenômeno da Internet cumpre um papel essencial na formulação do fluxo de informações. Assim mesmo, a competitividade nas transações comerciais maximiza as possibilidades por conta do sistema de participação geral. Por outro lado, o desafiador cenário globalizado obstaculiza a apreciação da importância dos modos de operação convencionais.
                  Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se o consenso sobre a necessidade de qualificação facilita a criação de alternativas às soluções ortodoxas. No mundo atual, o entendimento das metas propostas assume importantes posições no estabelecimento de todos os recursos funcionais envolvidos. Todavia, a percepção das dificuldades causa impacto indireto na reavaliação dos relacionamentos verticais entre as hierarquias. A certificação de metodologias que nos auxiliam a lidar com o início da atividade geral de formação de atitudes apresenta tendências no sentido de aprovar a manutenção dos conhecimentos estratégicos para atingir a excelência.
                  Caros amigos, a determinação clara de objetivos acarreta um processo de reformulação e modernização do impacto na agilidade decisória. Pensando mais a longo prazo, a contínua expansão de nossa atividade não pode mais se dissociar dos índices pretendidos. Ainda assim, existem dúvidas a respeito de como a consolidação das estruturas prepara-nos para enfrentar situações atípicas decorrentes dos níveis de motivação departamental. Gostaria de enfatizar que a constante divulgação das informações estimula a padronização dos procedimentos normalmente adotados. Neste sentido, a hegemonia do ambiente político talvez venha a ressaltar a relatividade das condições inegavelmente apropriadas.
                  É importante questionar o quanto a expansão dos mercados mundiais afeta positivamente a correta previsão do levantamento das variáveis envolvidas. Por conseguinte, o comprometimento entre as equipes garante a contribuição de um grupo importante na determinação das novas proposições. O cuidado em identificar pontos críticos na adoção de políticas descentralizadoras desafia a capacidade de equalização das regras de conduta normativas. O que temos que ter sempre em mente é que o desenvolvimento contínuo de distintas formas de atuação representa uma abertura para a melhoria do sistema de formação de quadros que corresponde às necessidades.
                  Percebemos, cada vez mais, que o surgimento do comércio virtual auxilia a preparação e a composição das diretrizes de desenvolvimento para o futuro. Podemos já vislumbrar o modo pelo qual a execução dos pontos do programa faz parte de um processo de gerenciamento das direções preferenciais no sentido do progresso. Evidentemente, a revolução dos costumes agrega valor ao estabelecimento do orçamento setorial. Do mesmo modo, o aumento do diálogo entre os diferentes setores produtivos pode nos levar a considerar a reestruturação dos paradigmas corporativos.
                  Desta maneira, o acompanhamento das preferências de consumo ainda não demonstrou convincentemente que vai participar na mudança das formas de ação. No entanto, não podemos esquecer que a consulta aos diversos militantes possibilita uma melhor visão global dos métodos utilizados na avaliação de resultados. Não obstante, a necessidade de renovação processual deve passar por modificações independentemente das condições financeiras e administrativas exigidas. Acima de tudo, é fundamental ressaltar que a crescente influência da mídia é uma das consequências das posturas dos órgãos dirigentes com relação às suas atribuições. É claro que o novo modelo estrutural aqui preconizado exige a precisão e a definição do remanejamento dos quadros funcionais.
                  O empenho em analisar o julgamento imparcial das eventualidades aponta para a melhoria do retorno esperado a longo prazo. A prática cotidiana prova que a estrutura atual da organização nos obriga à análise das diversas correntes de pensamento. As experiências acumuladas demonstram que a complexidade dos estudos efetuados promove a alavancagem do investimento em reciclagem técnica. O incentivo ao avanço tecnológico, assim como a mobilidade dos capitais internacionais estende o alcance e a importância da gestão inovadora da qual fazemos parte.
                  A nível organizacional, a valorização de fatores subjetivos oferece uma interessante oportunidade para verificação do processo de comunicação como um todo. Assim mesmo, o fenômeno da Internet cumpre um papel essencial na formulação das regras de conduta normativas. Podemos já vislumbrar o modo pelo qual a competitividade nas transações comerciais afeta positivamente a correta previsão de todos os recursos funcionais envolvidos. O incentivo ao avanço tecnológico, assim como o desafiador cenário globalizado obstaculiza a apreciação da importância de alternativas às soluções ortodoxas. O que temos que ter sempre em mente é que o consenso sobre a necessidade de qualificação facilita a criação do processo de comunicação como um todo.
                  É importante questionar o quanto o aumento do diálogo entre os diferentes setores produtivos prepara-nos para enfrentar situações atípicas decorrentes do sistema de formação de quadros que corresponde às necessidades. Todavia, o comprometimento entre as equipes deve passar por modificações independentemente dos conhecimentos estratégicos para atingir a excelência. Do mesmo modo, o desenvolvimento contínuo de distintas formas de atuação representa uma abertura para a melhoria dos modos de operação convencionais. Caros amigos, a hegemonia do ambiente político faz parte de um processo de gerenciamento dos métodos utilizados na avaliação de resultados. Pensando mais a longo prazo, a contínua expansão de nossa atividade acarreta um processo de reformulação e modernização dos relacionamentos verticais entre as hierarquias.
                  Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se a mobilidade dos capitais internacionais auxilia a preparação e a composição do fluxo de informações. No entanto, não podemos esquecer que a expansão dos mercados mundiais oferece uma interessante oportunidade para verificação da gestão inovadora da qual fazemos parte. Neste sentido, o julgamento imparcial das eventualidades estende o alcance e a importância dos níveis de motivação departamental.
                  No mundo atual, a complexidade dos estudos efetuados desafia a capacidade de equalização das diretrizes de desenvolvimento para o futuro. Acima de tudo, é fundamental ressaltar que a determinação clara de objetivos garante a contribuição de um grupo importante na determinação das novas proposições. As experiências acumuladas demonstram que a adoção de políticas descentralizadoras promove a alavancagem dos índices pretendidos.
                  Não obstante, a consolidação das estruturas ainda não demonstrou convincentemente que vai participar na mudança do remanejamento dos quadros funcionais. Por outro lado, a execução dos pontos do programa estimula a padronização do levantamento das variáveis envolvidas. Por conseguinte, o início da atividade geral de formação de atitudes assume importantes posições no estabelecimento das diversas correntes de pensamento. Evidentemente, o entendimento das metas propostas é uma das consequências do orçamento setorial.
                  Ainda assim, existem dúvidas a respeito de como a consulta aos diversos militantes agrega valor ao estabelecimento do retorno esperado a longo prazo. Desta maneira, o acompanhamento das preferências de consumo apresenta tendências no sentido de aprovar a manutenção das formas de ação. Gostaria de enfatizar que a constante divulgação das informações possibilita uma melhor visão global dos procedimentos normalmente adotados.
                  A certificação de metodologias que nos auxiliam a lidar com a valorização de fatores subjetivos pode nos levar a considerar a reestruturação das condições financeiras e administrativas exigidas. É claro que a necessidade de renovação processual talvez venha a ressaltar a relatividade das posturas dos órgãos dirigentes com relação às suas atribuições. A nível organizacional, o novo modelo estrutural aqui preconizado exige a precisão e a definição das condições inegavelmente apropriadas.
                  A prática cotidiana prova que o surgimento do comércio virtual maximiza as possibilidades por conta dos paradigmas corporativos. O empenho em analisar a estrutura atual da organização nos obriga à análise das direções preferenciais no sentido do progresso. O cuidado em identificar pontos críticos na revolução dos costumes causa impacto indireto na reavaliação do investimento em reciclagem técnica.
                  Percebemos, cada vez mais, que a crescente influência da mídia aponta para a melhoria do sistema de participação geral. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que a percepção das dificuldades não pode mais se dissociar do impacto na agilidade decisória. É importante questionar o quanto o acompanhamento das preferências de consumo afeta positivamente a correta previsão das regras de conduta normativas.
                  Não obstante, o comprometimento entre as equipes faz parte de um processo de gerenciamento do sistema de participação geral. Acima de tudo, é fundamental ressaltar que o consenso sobre a necessidade de qualificação estende o alcance e a importância dos modos de operação convencionais. O que temos que ter sempre em mente é que o início da atividade geral de formação de atitudes causa impacto indireto na reavaliação do processo de comunicação como um todo. Por outro lado, a estrutura atual da organização acarreta um processo de reformulação e modernização do fluxo de informações.
                  Ainda assim, existem dúvidas a respeito de como a consolidação das estruturas agrega valor ao estabelecimento das diversas correntes de pensamento. Do mesmo modo, o desenvolvimento contínuo de distintas formas de atuação nos obriga à análise das posturas dos órgãos dirigentes com relação às suas atribuições. Assim mesmo, a constante divulgação das informações representa uma abertura para a melhoria dos métodos utilizados na avaliação de resultados.
                  Pensando mais a longo prazo, a determinação clara de objetivos maximiza as possibilidades por conta dos relacionamentos verticais entre as hierarquias. Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se a mobilidade dos capitais internacionais auxilia a preparação e a composição das direções preferenciais no sentido do progresso. É claro que o novo modelo estrutural aqui preconizado obstaculiza a apreciação da importância do sistema de formação de quadros que corresponde às necessidades. Neste sentido, o julgamento imparcial das eventualidades assume importantes posições no estabelecimento dos conhecimentos estratégicos para atingir a excelência. Podemos já vislumbrar o modo pelo qual a hegemonia do ambiente político é uma das consequências dos níveis de motivação departamental.
                  Percebemos, cada vez mais, que a revolução dos costumes garante a contribuição de um grupo importante na determinação da gestão inovadora da qual fazemos parte. As experiências acumuladas demonstram que a adoção de políticas descentralizadoras promove a alavancagem de alternativas às soluções ortodoxas. A nível organizacional, a complexidade dos estudos efetuados ainda não demonstrou convincentemente que vai participar na mudança dos índices pretendidos. No mundo atual, a execução dos pontos do programa deve passar por modificações independentemente das novas proposições.
                  Por conseguinte, a competitividade nas transações comerciais não pode mais se dissociar das diretrizes de desenvolvimento para o futuro. Gostaria de enfatizar que a valorização de fatores subjetivos desafia a capacidade de equalização do orçamento setorial. Todavia, o surgimento do comércio virtual oferece uma interessante oportunidade para verificação do retorno esperado a longo prazo.
                  A certificação de metodologias que nos auxiliam a lidar com a crescente influência da mídia apresenta tendências no sentido de aprovar a manutenção dos paradigmas corporativos. O cuidado em identificar pontos críticos na necessidade de renovação processual possibilita uma melhor visão global dos procedimentos normalmente adotados. Desta maneira, a consulta aos diversos militantes facilita a criação das formas de ação.
                  No entanto, não podemos esquecer que o entendimento das metas propostas talvez venha a ressaltar a relatividade de todos os recursos funcionais envolvidos. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que a expansão dos mercados mundiais exige a precisão e a definição das condições financeiras e administrativas exigidas. O incentivo ao avanço tecnológico, assim como o fenômeno da Internet estimula a padronização do investimento em reciclagem técnica.
                  A prática cotidiana prova que o aumento do diálogo entre os diferentes setores produtivos prepara-nos para enfrentar situações atípicas decorrentes das condições inegavelmente apropriadas. O empenho em analisar a contínua expansão de nossa atividade cumpre um papel essencial na formulação do remanejamento dos quadros funcionais. Evidentemente, o desafiador cenário globalizado aponta para a melhoria do levantamento das variáveis envolvidas.
                  Caros amigos, a percepção das dificuldades pode nos levar a considerar a reestruturação do impacto na agilidade decisória. Por outro lado, o acompanhamento das preferências de consumo afeta positivamente a correta previsão dos paradigmas corporativos. É importante questionar o quanto a consulta aos diversos militantes faz parte de um processo de gerenciamento do remanejamento dos quadros funcionais.
                  Nunca é demais lembrar o peso e o significado destes problemas, uma vez que a consolidação das estruturas não pode mais se dissociar dos níveis de motivação departamental. No entanto, não podemos esquecer que a expansão dos mercados mundiais talvez venha a ressaltar a relatividade de todos os recursos funcionais envolvidos. A prática cotidiana prova que o julgamento imparcial das eventualidades estimula a padronização do fluxo de informações. Ainda assim, existem dúvidas a respeito de como o entendimento das metas propostas agrega valor ao estabelecimento das direções preferenciais no sentido do progresso. Podemos já vislumbrar o modo pelo qual o consenso sobre a necessidade de qualificação cumpre um papel essencial na formulação das posturas dos órgãos dirigentes com relação às suas atribuições.
                  Do mesmo modo, o início da atividade geral de formação de atitudes pode nos levar a considerar a reestruturação do sistema de formação de quadros que corresponde às necessidades. Pensando mais a longo prazo, o aumento do diálogo entre os diferentes setores produtivos maximiza as possibilidades por conta dos procedimentos normalmente adotados. Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se a determinação clara de objetivos estende o alcance e a importância do orçamento setorial. Gostaria de enfatizar que o novo modelo estrutural aqui preconizado obstaculiza a apreciação da importância das condições financeiras e administrativas exigidas. Por conseguinte, a mobilidade dos capitais internacionais prepara-nos para enfrentar situações atípicas decorrentes dos modos de operação convencionais.
                  Evidentemente, a hegemonia do ambiente político acarreta um processo de reformulação e modernização do retorno esperado a longo prazo. Assim mesmo, a contínua expansão de nossa atividade garante a contribuição de um grupo importante na determinação do sistema de participação geral. As experiências acumuladas demonstram que a adoção de políticas descentralizadoras causa impacto indireto na reavaliação das diretrizes de desenvolvimento para o futuro. A nível organizacional, o fenômeno da Internet ainda não demonstrou convincentemente que vai participar na mudança dos índices pretendidos.
                  No mundo atual, a execução dos pontos do programa possibilita uma melhor visão global das novas proposições. A certificação de metodologias que nos auxiliam a lidar com a competitividade nas transações comerciais aponta para a melhoria do processo de comunicação como um todo. É claro que a percepção das dificuldades desafia a capacidade de equalização das diversas correntes de pensamento. O incentivo ao avanço tecnológico, assim como o desenvolvimento contínuo de distintas formas de atuação assume importantes posições no estabelecimento dos relacionamentos verticais entre as hierarquias.
                  Acima de tudo, é fundamental ressaltar que a crescente influência da mídia deve passar por modificações independentemente das regras de conduta normativas. O cuidado em identificar pontos críticos na necessidade de renovação processual exige a precisão e a definição da gestão inovadora da qual fazemos parte. Desta maneira, o comprometimento entre as equipes apresenta tendências no sentido de aprovar a manutenção do impacto na agilidade decisória. Caros amigos, o surgimento do comércio virtual promove a alavancagem de alternativas às soluções ortodoxas.
                  Não obstante, a revolução dos costumes oferece uma interessante oportunidade para verificação dos métodos utilizados na avaliação de resultados. Neste sentido, o desafiador cenário globalizado nos obriga à análise do investimento em reciclagem técnica. Todavia, a estrutura atual da organização auxilia a preparação e a composição das condições inegavelmente apropriadas.                  O empenho em analisar a constante divulgação das informações é uma das consequências das formas de ação. Percebemos, cada vez mais, que a complexidade dos estudos efetuados facilita a criação do levantamento das variáveis envolvidas. O que temos que ter sempre em mente é que a valorização de fatores subjetivos representa uma abertura para a melhoria dos conhecimentos estratégicos para atingir a excelência. No mundo atual, a valorização de fatores subjetivos auxilia a preparação e a composição dos paradigmas corporativos.
                  Assim mesmo, a complexidade dos estudos efetuados estimula a padronização do remanejamento dos quadros funcionais. O que temos que ter sempre em mente é que a consolidação das estruturas oferece uma interessante oportunidade para verificação dos níveis de motivação departamental. O incentivo ao avanço tecnológico, assim como a expansão dos mercados mundiais talvez venha a ressaltar a relatividade dos conhecimentos estratégicos para atingir a excelência. Percebemos, cada vez mais, que o julgamento imparcial das eventualidades não pode mais se dissociar dos relacionamentos verticais entre as hierarquias. Podemos já vislumbrar o modo pelo qual a percepção das dificuldades agrega valor ao estabelecimento das diversas correntes de pensamento.
                  Acima de tudo, é fundamental ressaltar que a hegemonia do ambiente político promove a alavancagem das diretrizes de desenvolvimento para o futuro. Caros amigos, a necessidade de renovação processual pode nos levar a considerar a reestruturação do sistema de formação de quadros que corresponde às necessidades. Pensando mais a longo prazo, o aumento do diálogo entre os diferentes setores produtivos desafia a capacidade de equalização das condições inegavelmente apropriadas. Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se a determinação clara de objetivos estende o alcance e a importância do impacto na agilidade decisória.
                  Gostaria de enfatizar que a revolução dos costumes prepara-nos para enfrentar situações atípicas decorrentes das novas proposições. É claro que a adoção de políticas descentralizadoras obstaculiza a apreciação da importância do retorno esperado a longo prazo. Por outro lado, o fenômeno da Internet deve passar por modificações independentemente do fluxo de informações. A certificação de metodologias que nos auxiliam a lidar com a contínua expansão de nossa atividade maximiza as possibilidades por conta das posturas dos órgãos dirigentes com relação às suas atribuições.
                  É importante questionar o quanto o entendimento das metas propostas ainda não demonstrou convincentemente que vai participar na mudança das direções preferenciais no sentido do progresso. Desta maneira, a execução dos pontos do programa possibilita uma melhor visão global de alternativas às soluções ortodoxas. No entanto, não podemos esquecer que o desafiador cenário globalizado acarreta um processo de reformulação e modernização das condições financeiras e administrativas exigidas. Por conseguinte, o desenvolvimento contínuo de distintas formas de atuação exige a precisão e a definição das formas de ação.
                  A prática cotidiana prova que o comprometimento entre as equipes garante a contribuição de um grupo importante na determinação do sistema de participação geral. As experiências acumuladas demonstram que a mobilidade dos capitais internacionais afeta positivamente a correta previsão do orçamento setorial. Ainda assim, existem dúvidas a respeito de como o surgimento do comércio virtual cumpre um papel essencial na formulação dos índices pretendidos.
                  A nível organizacional, a consulta aos diversos militantes assume importantes posições no estabelecimento da gestão inovadora da qual fazemos parte. O cuidado em identificar pontos críticos na constante divulgação das informações apresenta tendências no sentido de aprovar a manutenção dos modos de operação convencionais. Do mesmo modo, a crescente influência da mídia faz parte de um processo de gerenciamento das regras de conduta normativas.
                  Não obstante, o acompanhamento das preferências de consumo aponta para a melhoria de todos os recursos funcionais envolvidos. Neste sentido, o novo modelo estrutural aqui preconizado nos obriga à análise do investimento em reciclagem técnica. Todavia, a estrutura atual da organização causa impacto indireto na reavaliação do processo de comunicação como um todo. O empenho em analisar a competitividade nas transações comerciais é uma das consequências dos procedimentos normalmente adotados.
                  Evidentemente, o início da atividade geral de formação de atitudes representa uma abertura para a melhoria dos métodos utilizados na avaliação de resultados. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que o consenso sobre a necessidade de qualificação facilita a criação do levantamento das variáveis envolvidas. No mundo atual, a determinação clara de objetivos maximiza as possibilidades por conta das regras de conduta normativas. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que a competitividade nas transações comerciais causa impacto indireto na reavaliação do remanejamento dos quadros funcionais. O incentivo ao avanço tecnológico, assim como a consolidação das estruturas representa uma abertura para a melhoria dos índices pretendidos.
                  O que temos que ter sempre em mente é que o desafiador cenário globalizado cumpre um papel essencial na formulação do levantamento das variáveis envolvidas. Ainda assim, existem dúvidas a respeito de como a percepção das dificuldades não pode mais se dissociar dos procedimentos normalmente adotados. Assim mesmo, a expansão dos mercados mundiais pode nos levar a considerar a reestruturação dos relacionamentos verticais entre as hierarquias.
                  Acima de tudo, é fundamental ressaltar que a complexidade dos estudos efetuados estimula a padronização das diretrizes de desenvolvimento para o futuro. Caros amigos, a necessidade de renovação processual é uma das consequências das condições financeiras e administrativas exigidas. É claro que o aumento do diálogo entre os diferentes setores produtivos estende o alcance e a importância das condições inegavelmente apropriadas. Evidentemente, a valorização de fatores subjetivos exige a precisão e a definição do impacto na agilidade decisória.
                  Gostaria de enfatizar que a revolução dos costumes prepara-nos para enfrentar situações atípicas decorrentes das novas proposições. Por outro lado, a adoção de políticas descentralizadoras afeta positivamente a correta previsão do retorno esperado a longo prazo. A prática cotidiana prova que o fenômeno da Internet deve passar por modificações independentemente do orçamento setorial. Desta maneira, o acompanhamento das preferências de consumo auxilia a preparação e a composição das diversas correntes de pensamento.
                  Neste sentido, o entendimento das metas propostas assume importantes posições no estabelecimento das direções preferenciais no sentido do progresso. O empenho em analisar a execução dos pontos do programa possibilita uma melhor visão global de alternativas às soluções ortodoxas. Podemos já vislumbrar o modo pelo qual o novo modelo estrutural aqui preconizado acarreta um processo de reformulação e modernização das posturas dos órgãos dirigentes com relação às suas atribuições.
                  Por conseguinte, a hegemonia do ambiente político desafia a capacidade de equalização dos conhecimentos estratégicos para atingir a excelência. As experiências acumuladas demonstram que a estrutura atual da organização promove a alavancagem da gestão inovadora da qual fazemos parte. É importante questionar o quanto a mobilidade dos capitais internacionais garante a contribuição de um grupo importante na determinação do sistema de participação geral. Percebemos, cada vez mais, que o julgamento imparcial das eventualidades talvez venha a ressaltar a relatividade dos níveis de motivação departamental.
                  A nível organizacional, a consulta aos diversos militantes ainda não demonstrou convincentemente que vai participar na mudança de todos os recursos funcionais envolvidos. Pensando mais a longo prazo, a constante divulgação das informações apresenta tendências no sentido de aprovar a manutenção dos modos de operação convencionais. Do mesmo modo, a crescente influência da mídia oferece uma interessante oportunidade para verificação dos paradigmas corporativos. Não obstante, o desenvolvimento contínuo de distintas formas de atuação aponta para a melhoria do fluxo de informações.
                  A certificação de metodologias que nos auxiliam a lidar com a contínua expansão de nossa atividade facilita a criação do sistema de formação de quadros que corresponde às necessidades. Todavia, o consenso sobre a necessidade de qualificação faz parte de um processo de gerenciamento dos métodos utilizados na avaliação de resultados. No entanto, não podemos esquecer que o início da atividade geral de formação de atitudes nos obriga à análise do investimento em reciclagem técnica. O cuidado em identificar pontos críticos no surgimento do comércio virtual obstaculiza a apreciação da importância do processo de comunicação como um todo. Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se o comprometimento entre as equipes agrega valor ao estabelecimento das formas de ação.
                  O que temos que ter sempre em mente é que a execução dos pontos do programa é uma das consequências de alternativas às soluções ortodoxas. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que a mobilidade dos capitais internacionais auxilia a preparação e a composição do remanejamento dos quadros funcionais. No entanto, não podemos esquecer que o consenso sobre a necessidade de qualificação ainda não demonstrou convincentemente que vai participar na mudança dos conhecimentos estratégicos para atingir a excelência. As experiências acumuladas demonstram que o desafiador cenário globalizado talvez venha a ressaltar a relatividade do levantamento das variáveis envolvidas. Todavia, a revolução dos costumes prepara-nos para enfrentar situações atípicas decorrentes dos procedimentos normalmente adotados.
                  Desta maneira, a expansão dos mercados mundiais garante a contribuição de um grupo importante na determinação das posturas dos órgãos dirigentes com relação às suas atribuições. Caros amigos, o novo modelo estrutural aqui preconizado afeta positivamente a correta previsão das regras de conduta normativas. Pensando mais a longo prazo, a crescente influência da mídia maximiza as possibilidades por conta do orçamento setorial.
                  O incentivo ao avanço tecnológico, assim como a contínua expansão de nossa atividade nos obriga à análise das condições inegavelmente apropriadas. Por conseguinte, a percepção das dificuldades exige a precisão e a definição do sistema de participação geral. Gostaria de enfatizar que a valorização de fatores subjetivos pode nos levar a considerar a reestruturação das novas proposições.
                  Acima de tudo, é fundamental ressaltar que a complexidade dos estudos efetuados representa uma abertura para a melhoria do retorno esperado a longo prazo. Percebemos, cada vez mais, que o acompanhamento das preferências de consumo deve passar por modificações independentemente das condições financeiras e administrativas exigidas. Assim mesmo, o aumento do diálogo entre os diferentes setores produtivos facilita a criação das direções preferenciais no sentido do progresso.
                  Neste sentido, a hegemonia do ambiente político assume importantes posições no estabelecimento das diretrizes de desenvolvimento para o futuro. É importante questionar o quanto o desenvolvimento contínuo de distintas formas de atuação estimula a padronização das formas de ação. Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se a adoção de políticas descentralizadoras causa impacto indireto na reavaliação dos índices pretendidos. No mundo atual, a determinação clara de objetivos desafia a capacidade de equalização do sistema de formação de quadros que corresponde às necessidades.
                  É claro que a estrutura atual da organização acarreta um processo de reformulação e modernização do processo de comunicação como um todo. A certificação de metodologias que nos auxiliam a lidar com o fenômeno da Internet estende o alcance e a importância do impacto na agilidade decisória.
        """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'description' must be between 1 and 255 characters";
    final var actualVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );
    final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

    // when
    final var actualErrors = Assertions.assertThrows(
        DomainException.class,
        () -> validator.validate()
    );

    // then
    Assertions.assertEquals(expectedErrorCount, actualErrors.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualErrors.getErrors().get(0).message());
  }

  @Test
  public void givenNullLaunchedAt_whenCallsValidate_shouldReceiveError() {
    // given
    final var expectedTitle = "System Design Interview";
    final var expectedDescription = "description";
    final Year expectedLaunchedAt = null;
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'launchedAt' should not be null";
    final var actualVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );
    final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

    // when
    final var actualErrors = Assertions.assertThrows(
        DomainException.class,
        () -> validator.validate()
    );

    // then
    Assertions.assertEquals(expectedErrorCount, actualErrors.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualErrors.getErrors().get(0).message());
  }

  @Test
  public void givenNullRating_whenCallsValidate_shouldReceiveError() {
    // given
    final var expectedTitle = "System Design Interview";
    final var expectedDescription = "A description";
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final Rating expectedRating = null;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'rating' should not be null";
    final var actualVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );
    final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

    // when
    final var actualErrors = Assertions.assertThrows(
        DomainException.class,
        () -> validator.validate()
    );

    // then
    Assertions.assertEquals(expectedErrorCount, actualErrors.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualErrors.getErrors().get(0).message());
  }
}
