import matplotlib.pyplot as plt
import numpy as np


# Régression linéaire


def plot_courbe(taille, resolution_time):
    pente, ordonnee_origine = np.polyfit(taille, resolution_time, 1)

#    Calcul de la droite de tendance
    droite_tendance = pente * np.array(taille) + ordonnee_origine

    # Création du graphique
    fig, ax = plt.subplots(figsize=(10, 6))

    # Définir la taille du graphique
    # Tracé des données avec des marqueurs et une ligne
    ax.plot(taille, resolution_time, marker='o', linestyle='-', color='#228B22', label='Temps de résolution', linewidth=1.5) 

    # Tracé de la droite de tendance
    ax.plot(taille, droite_tendance, linestyle='--', color='red', label='Droite de tendance', linewidth=2)

    # Configuration des axes
    ax.set_xlabel("Taille du carré (n²)", fontsize=14)
    ax.set_ylabel("Temps de résolution (secondes)", fontsize=14)
    ax.set_title("Relation entre la taille du carré et le temps de résolution", fontsize=16)

    # Ajouter une légende
    ax.legend(fontsize=12, loc='upper left')

    # Définir le format des ticks et des labels
    ax.tick_params(axis='both', labelsize=12)

    # Formater les axes avec des grilles
    ax.grid(True, which='both', linestyle='--', alpha=0.7)

    # Ajuster la disposition du graphique
    plt.tight_layout()
    plt.text(100, 30, f"y = {(pente*100):.2f}e-2x + {ordonnee_origine:.2f}", fontsize=12, color='red')
    print(pente, ordonnee_origine)
    # Affichage du graphique
    plt.show()


if __name__ == '__main__':

# Données du tableau
    taille = [9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 324, 361, 400, 441, 484, 529, 576, 625, 676]
    resolution_time = [0.034, 0.028, 0.029, 0.060, 0.086, 0.109, 0.162, 0.258, 0.309, 0.453, 0.683, 0.919, 1.280, 1.774, 2.418, 3.340, 4.681, 6.462, 8.333, 11.030, 13.990, 20.439, 24.485, 31.567 ]

    # Calcul de la taille des carrés
    taille_carre = [x**2 for x in taille]
    plot_courbe(taille_carre, resolution_time)