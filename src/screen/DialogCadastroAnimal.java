package screen;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import data.Animal;

public class DialogCadastroAnimal extends JDialog {

    private static final Color VERDE= new Color(46, 125, 50);
    private static final Color VERDE_HOVER = new Color(27, 94, 32);
    private static final Color FUNDO= new Color(248, 250, 248);
    private static final Color BRANCO= Color.WHITE;
    private static final Color CINZA_BORDA = new Color(220, 220, 220);
    private static final Color CINZA_TEXTO = new Color(150, 150, 150);

    private JTextField campoNome;
    private JComboBox<String> comboEspecie;
    private JComboBox<String> comboPorte;
    private JComboBox<String> comboSexo;
    private JTextField campoRaca;
    private JComboBox<String> comboStatus;
    private Runnable refreshCallback;
    private JLabel lblFoto;
    private String caminhoImagem;

    public DialogCadastroAnimal(Window pai, Runnable refreshCallback) {
        super(pai, "Cadastrar Animal", ModalityType.APPLICATION_MODAL);
        this.refreshCallback = refreshCallback;
        setSize(480, 560);
        setLocationRelativeTo(pai);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);

        add(criarCabecalho(),  BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(criarFormulario());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
        add(criarBotoes(),     BorderLayout.SOUTH);
    }

    // ── CABEÇALHO ─────────────────────────────────────────────────────────────
    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setBackground(VERDE);
        cab.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel titulo = new JLabel("Cadastrar Animal");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BRANCO);

        JLabel sub = new JLabel("Preencha os dados do animal");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(200, 230, 200));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(sub);

        cab.add(textos, BorderLayout.CENTER);
        return cab;
    }

    // ── FORMULÁRIO ────────────────────────────────────────────────────────────
    private JPanel criarFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(FUNDO);
        form.setBorder(new EmptyBorder(24, 28, 8, 28));

        campoNome = criarCampoTextoComponente("Ex: Mel");
        comboEspecie = criarCampoComboComponente(new String[]{"Selecione...", "Cachorro", "Gato", "Coelho", "Outro"});
        comboPorte = criarCampoComboComponente(new String[]{"Selecione...", "Pequeno", "Médio", "Grande"});
        comboSexo = criarCampoComboComponente(new String[]{"Selecione...", "Macho", "Fêmea"});
        campoRaca = criarCampoTextoComponente("Ex: Labrador (opcional)");
        comboStatus = criarCampoComboComponente(new String[]{"Disponível", "Adotado"});

        form.add(criarGrupoFiltro("Nome do animal *", campoNome));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupoFiltro("Espécie *", comboEspecie));
        form.add(Box.createVerticalStrut(12));
        form.add(criarPainelFoto());
        form.add(Box.createVerticalStrut(14));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupoFiltro("Porte *", comboPorte));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupoFiltro("Sexo", comboSexo));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupoFiltro("Raça", campoRaca));
        form.add(Box.createVerticalStrut(14));
        form.add(criarGrupoFiltro("Status *", comboStatus));

        return form;
    }

    private JTextField criarCampoTextoComponente(String placeholder) {
        JTextField campo = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setForeground(CINZA_TEXTO);
        campo.setBorder(new EmptyBorder(10, 12, 10, 12));
        campo.setOpaque(false);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campo.setAlignmentX(LEFT_ALIGNMENT);
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(CINZA_TEXTO);
                }
            }
        });
        return campo;
    }

    private JComboBox<String> criarCampoComboComponente(String[] opcoes) {
        JComboBox<String> combo = new JComboBox<>(opcoes);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(BRANCO);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        combo.setAlignmentX(LEFT_ALIGNMENT);
        return combo;
    }

    private JPanel criarGrupoFiltro(String rotulo, JComponent campo) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setOpaque(false);
        grupo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(5));
        grupo.add(campo);
        return grupo;
    }

    private JPanel criarPainelFoto() {

    JPanel grupo = new JPanel();
    grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
    grupo.setOpaque(false);

    JLabel titulo = new JLabel("Foto do Animal");
    titulo.setFont(new Font("Segoe UI", Font.BOLD, 13));

    lblFoto = new JLabel("Nenhuma imagem");
    lblFoto.setAlignmentX(Component.LEFT_ALIGNMENT);
    lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
    lblFoto.setPreferredSize(new Dimension(120, 120));
    lblFoto.setMaximumSize(new Dimension(120, 120));
    lblFoto.setBorder(new LineBorder(CINZA_BORDA));

    JButton btnImagem = new JButton("Selecionar Imagem");

    btnImagem.addActionListener(e -> {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
        "Imagens (*.jpg, *.jpeg, *.png)",
        "jpg",
        "jpeg",
        "png"
            )
        );

        int resultado = chooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            try {

                File arquivoOriginal =
                    chooser.getSelectedFile();

                String nomeArquivo =
                    arquivoOriginal.getName();

                Path destino =
                    Path.of("imagens", nomeArquivo);

                Files.copy(
                    arquivoOriginal.toPath(),
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
                );

                caminhoImagem = destino.toString();

                ImageIcon icon =
                    new ImageIcon(caminhoImagem);

                Image imagem =
                    icon.getImage().getScaledInstance(
                        120,
                        120,
                        Image.SCALE_SMOOTH
                    );

                lblFoto.setText("");
                lblFoto.setIcon(new ImageIcon(imagem));

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    this,
                    "Erro ao copiar imagem!"
                );
            }
        }
    });

    grupo.add(titulo);
    grupo.add(Box.createVerticalStrut(5));
    grupo.add(lblFoto);
    grupo.add(Box.createVerticalStrut(8));
    grupo.add(btnImagem);

    return grupo;
}

    // ── BOTÕES ────────────────────────────────────────────────────────────────
    private JPanel criarBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        painel.setBackground(FUNDO);
        painel.setBorder(new MatteBorder(1, 0, 0, 0, CINZA_BORDA));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setBackground(new Color(238, 238, 238));
        btnCancelar.setForeground(new Color(60, 60, 60));
        btnCancelar.setBorder(new CompoundBorder(
            new LineBorder(CINZA_BORDA, 1, true),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnSalvar = new JButton("Salvar Animal") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? VERDE_HOVER : VERDE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setForeground(BRANCO);
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorder(new EmptyBorder(10, 24, 10, 24));
        btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnSalvar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            if (nome.isEmpty() || nome.equals("Ex: Mel")) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String especie = comboEspecie.getSelectedItem().toString();
            if (especie.equals("Selecione...")) {
                JOptionPane.showMessageDialog(this, "Espécie é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String porte = comboPorte.getSelectedItem().toString();
            if (porte.equals("Selecione...")) {
                JOptionPane.showMessageDialog(this, "Porte é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String status = comboStatus.getSelectedItem().toString();
            String raca = campoRaca.getText().trim();
            if (raca.equals("Ex: Labrador (opcional)")) {
                raca = "";
            }

            if (caminhoImagem == null || caminhoImagem.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma foto do animal!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            return;
            }

            Animal animal = new Animal(
                nome,
                new Date(),
                especie,
                raca,
                porte,
                false,
                "Adotado".equals(status),
                caminhoImagem
            );

            utils.SistemaDados.animais.add(animal);
            utils.SistemaDados.save();

            JOptionPane.showMessageDialog(this,
                "Animal cadastrado com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            if (refreshCallback != null) {
                refreshCallback.run();
            }
            dispose();
        });

        painel.add(btnCancelar);
        painel.add(btnSalvar);
        return painel;
    }
}