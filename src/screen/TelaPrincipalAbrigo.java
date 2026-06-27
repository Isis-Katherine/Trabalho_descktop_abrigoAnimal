package screen;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import utils.MenuPainel;
import utils.SistemaDados;
import utils.MenuBotao;

public class TelaPrincipalAbrigo extends App {

    private final Color VERDE = new Color(46, 125, 50);
    private final Color FUNDO = new Color(248, 250, 248);

    // Painel central com CardLayout — só essa parte muda ao navegar
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel painelCentral = new JPanel(cardLayout);
    private PainelAdocao painelAdocao;
    private PainelAdotante painelAdotante;
    private PainelAnimais painelAnimais;
    private JPanel painelInicio;

    public TelaPrincipalAbrigo() {
        super("AbrigoPet - Sistema de Gerenciamento");

        // Registra cada "página" com um nome-chave
        painelCentral.setBackground(FUNDO);
        painelInicio = criarPainelInicio();
        painelCentral.add(painelInicio, "inicio");
        painelAnimais = new PainelAnimais();
        painelCentral.add(painelAnimais, "animais");
        painelAdocao = new PainelAdocao();
        painelAdotante = new PainelAdotante();

        painelCentral.add(painelAdocao, "adocao");
        painelCentral.add(painelAdotante, "adotante");

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(FUNDO);
        painelPrincipal.add(criarMenuLateral(), BorderLayout.WEST);
        painelPrincipal.add(painelCentral,      BorderLayout.CENTER);
        painelPrincipal.add(criarRodape(),      BorderLayout.SOUTH);
        add(painelPrincipal);

        setVisible(true);
    }

    // Troca a tela central sem mexer no menu ------------------------

    private void navegarPara(String tela) {

        if ("adotante".equals(tela) && painelAdotante != null) {
            painelAdotante.atualizarTabela();
        }
        if ("animais".equals(tela) && painelAnimais != null) {
             painelAnimais.atualizarTabela();
        }

        if ("inicio".equals(tela)) {

            painelCentral.remove(painelInicio);

            painelInicio = criarPainelInicio();

            painelCentral.add(painelInicio, "inicio");

            painelCentral.revalidate();
            painelCentral.repaint();
        }

        cardLayout.show(painelCentral, tela);
    }

    // ── MENU LATERAL ─────────────────────────────────────────────────────────
    private JPanel criarMenuLateral() {
        MenuPainel menu = new MenuPainel();
        menu.setPreferredSize(new Dimension(250, 800));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(40, 25, 40, 25));

        JLabel logo = new JLabel("AbrigoPet");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Cuidar é amar");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        MenuBotao btnInicio  = new MenuBotao("Inicio",  "/icons/MenuHome.png");
        MenuBotao btnAnimais = new MenuBotao("Animais", "/icons/MenuAnimais.png");
        MenuBotao btnAdocao  = new MenuBotao("Doação",  "/icons/MenuDoacao.png");
        MenuBotao btnAdotante = new MenuBotao("Adotante", "/icons/MenuAdotante.png");

        // Cada botão só troca o centro — sem abrir nova janela, sem dispose()
        btnInicio .addActionListener(e -> navegarPara("inicio"));
        btnAnimais.addActionListener(e -> navegarPara("animais"));
        btnAdocao .addActionListener(e -> navegarPara("adocao"));
        btnAdotante.addActionListener(e -> navegarPara("adotante"));

        JLabel rodape = new JLabel("Juntos por vidas melhores.");
        rodape.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rodape.setForeground(Color.WHITE);
        rodape.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu.add(Box.createVerticalStrut(50));
        menu.add(logo);
        menu.add(Box.createVerticalStrut(10));
        menu.add(subtitulo);
        menu.add(Box.createVerticalStrut(60));
        menu.add(btnInicio);
        menu.add(Box.createVerticalStrut(15));
        menu.add(btnAnimais);
        menu.add(Box.createVerticalStrut(15));
        menu.add(btnAdocao);
        menu.add(Box.createVerticalStrut(15));
        menu.add(btnAdotante);
        menu.add(Box.createVerticalStrut(15));
        menu.add(Box.createVerticalGlue());
        menu.add(rodape);

        return menu;
    }

    // ── PAINEL INÍCIO ────────────────────────────────────────────────────────
    private JPanel criarPainelInicio() {
        JPanel conteudo = new JPanel();
        conteudo.setBackground(FUNDO);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel titulo = new JLabel("Bem-vindo!");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 50));
        titulo.setForeground(VERDE);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descricao = new JLabel("Gerencie o abrigo de forma simples e eficiente.");
        descricao.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        descricao.setForeground(Color.DARK_GRAY);
        descricao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel painelResumo = new JPanel(new GridLayout(1, 3, 25, 25));
        painelResumo.setBackground(FUNDO);
        painelResumo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelResumo.add(criarCardResumo("Animais disponíveis",String.valueOf(utils.SistemaDados.getQuantidadeAnimaisDisponiveis()), "Para adoção"));
        painelResumo.add(criarCardResumo("Adoções do mês",String.valueOf(SistemaDados.getQuantidadeAdocoesMes()), "Este mês"));
        painelResumo.add(criarCardResumo("Taxa de adoação", SistemaDados.getTaxaAdocao() + "%", "Animais adotados"));

        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(10));
        conteudo.add(descricao);
        conteudo.add(Box.createVerticalStrut(40));
        conteudo.add(painelResumo);

        return conteudo;
    }

    // ── CARD RESUMO ──────────────────────────────────────────────────────────
    private JPanel criarCardResumo(String titulo, String numero, String descricao) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(225, 225, 225), 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel lblNumero = new JLabel(numero);
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 46));
        lblNumero.setForeground(VERDE);

        JLabel lblDescricao = new JLabel(descricao);
        lblDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(20));
        card.add(lblNumero);
        card.add(Box.createVerticalStrut(10));
        card.add(lblDescricao);

        return card;
    }

    // ── RODAPÉ ───────────────────────────────────────────────────────────────
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rodape.setBackground(new Color(245, 245, 245));
        rodape.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel texto = new JLabel(
            "AbrigoPet - Sistema de Gerenciamento de Abrigo de Animais | Versão 1.0"
        );
        texto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        texto.setForeground(Color.GRAY);
        rodape.add(texto);

        return rodape;
    }
}
