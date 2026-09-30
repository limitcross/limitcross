import 'package:flutter/material.dart';

enum ServiceCategory {
  all,
  homeMaintenance,
  cleaning,
  beautyWomen,
  mensGrooming,
  homeHelp,
  healthAtHome,
  nativeProducts,
}

extension ServiceCategoryExt on ServiceCategory {
  IconData get icon {
    switch (this) {
      case ServiceCategory.all:
        return Icons.grid_view_outlined;
      case ServiceCategory.homeMaintenance:
        return Icons.home_repair_service_outlined;
      case ServiceCategory.cleaning:
        return Icons.cleaning_services_outlined;
      case ServiceCategory.beautyWomen:
        return Icons.spa_outlined;
      case ServiceCategory.mensGrooming:
        return Icons.content_cut_outlined;
      case ServiceCategory.homeHelp:
        return Icons.home_outlined;
      case ServiceCategory.healthAtHome:
        return Icons.medical_services_outlined;
      case ServiceCategory.nativeProducts:
        return Icons.inventory_2_outlined;
    }
  }

  String get label {
    switch (this) {
      case ServiceCategory.all:
        return 'All Services';
      case ServiceCategory.homeMaintenance:
        return 'Home Maintenance';
      case ServiceCategory.cleaning:
        return 'Cleaning';
      case ServiceCategory.beautyWomen:
        return 'Beauty & Wellness';
      case ServiceCategory.mensGrooming:
        return "Men's Grooming";
      case ServiceCategory.homeHelp:
        return 'Home Help';
      case ServiceCategory.healthAtHome:
        return 'Health at Home';
      case ServiceCategory.nativeProducts:
        return 'Native Products';
    }
  }

  String get iconEmoji {
    switch (this) {
      case ServiceCategory.all:
        return '🛠';
      case ServiceCategory.homeMaintenance:
        return '🏠';
      case ServiceCategory.cleaning:
        return '🧹';
      case ServiceCategory.beautyWomen:
        return '💇';
      case ServiceCategory.mensGrooming:
        return '🧔';
      case ServiceCategory.homeHelp:
        return '🏡';
      case ServiceCategory.healthAtHome:
        return '🏥';
      case ServiceCategory.nativeProducts:
        return '📦';
    }
  }
}

class ServiceItem {
  final String id;
  final String title;
  final String emoji;
  final ServiceCategory category;
  final String description;
  final String priceRange;
  final int startingPrice;
  final double rating;
  final int reviewsCount;
  final String duration;
  final List<String> highlights;
  final bool isPopular;
  final Color accentColor;

  const ServiceItem({
    required this.id,
    required this.title,
    required this.emoji,
    required this.category,
    required this.description,
    required this.priceRange,
    required this.startingPrice,
    this.rating = 4.8,
    this.reviewsCount = 1240,
    this.duration = '45-90 mins',
    required this.highlights,
    this.isPopular = false,
    this.accentColor = const Color(0xFF4F46E5),
  });

  factory ServiceItem.fromApi(Map<String, dynamic> data) {
    final categoryName = (data['category'] ?? 'homeMaintenance').toString();
    final category = ServiceCategory.values.firstWhere(
      (value) => value.name == categoryName,
      orElse: () => ServiceCategory.homeMaintenance,
    );
    return ServiceItem(
      id: data['id'].toString(),
      title: data['title'].toString(),
      emoji: data['emoji'].toString(),
      category: category,
      description: data['description'].toString(),
      priceRange: data['priceRange'].toString(),
      startingPrice: (data['startingPrice'] as num).toInt(),
      rating: (data['rating'] as num?)?.toDouble() ?? 0,
      reviewsCount: (data['reviewsCount'] as num?)?.toInt() ?? 0,
      duration: data['duration'].toString(),
      highlights: (data['highlights'] as List<dynamic>? ?? const []).map((item) => item.toString()).toList(),
      isPopular: data['isPopular'] == true,
    );
  }

  static const List<ServiceItem> allServices = [
    // 🏠 Home Maintenance
    ServiceItem(
      id: 'hm-ac',
      title: 'AC Service & Repair',
      emoji: '❄️',
      category: ServiceCategory.homeMaintenance,
      description: 'Service, gas refill, deep clean, installation',
      priceRange: '₹400 – ₹2,500',
      startingPrice: 400,
      rating: 4.86,
      reviewsCount: 4520,
      duration: '45-60 mins',
      highlights: ['Power jet foam clean', 'Gas leak detection', '30-day warranty'],
      isPopular: true,
      accentColor: Color(0xFF0284C7),
    ),
    ServiceItem(
      id: 'hm-plumb',
      title: 'Plumbing',
      emoji: '🔧',
      category: ServiceCategory.homeMaintenance,
      description: 'Leaks, tap repair, pipe fitting, blockage',
      priceRange: '₹300 – ₹1,500',
      startingPrice: 300,
      rating: 4.81,
      reviewsCount: 3100,
      duration: '30-60 mins',
      highlights: ['Certified plumbers', 'Standardized rate card', 'Genuine spare parts'],
      accentColor: Color(0xFF2563EB),
    ),
    ServiceItem(
      id: 'hm-electric',
      title: 'Electrician',
      emoji: '⚡',
      category: ServiceCategory.homeMaintenance,
      description: 'Wiring, fan, switch, MCB repair',
      priceRange: '₹300 – ₹1,500',
      startingPrice: 300,
      rating: 4.84,
      reviewsCount: 3890,
      duration: '30-60 mins',
      highlights: ['Background verified pros', 'High grade equipment', 'Post-service test'],
      isPopular: true,
      accentColor: Color(0xFFD97706),
    ),
    ServiceItem(
      id: 'hm-carpenter',
      title: 'Carpentry',
      emoji: '🪚',
      category: ServiceCategory.homeMaintenance,
      description: 'Furniture repair, door fix, installation',
      priceRange: '₹400 – ₹2,000',
      startingPrice: 400,
      rating: 4.79,
      reviewsCount: 1940,
      duration: '45-90 mins',
      highlights: ['Precision tools', 'Custom fitting', 'Wood care consultation'],
      accentColor: Color(0xFFB45309),
    ),
    ServiceItem(
      id: 'hm-paint',
      title: 'Painting & Wall Décor',
      emoji: '🎨',
      category: ServiceCategory.homeMaintenance,
      description: 'Interior, exterior, texture, waterproofing',
      priceRange: '₹5,000 – ₹50,000',
      startingPrice: 5000,
      rating: 4.88,
      reviewsCount: 820,
      duration: '1-3 days',
      highlights: ['Laser measurement site visit', 'Low-VOC paints', 'Post-paint clean'],
      accentColor: Color(0xFFE11D48),
    ),
    ServiceItem(
      id: 'hm-appliance',
      title: 'Appliance Repair',
      emoji: '📺',
      category: ServiceCategory.homeMaintenance,
      description: 'TV, washing machine, fridge, microwave',
      priceRange: '₹400 – ₹2,000',
      startingPrice: 400,
      rating: 4.82,
      reviewsCount: 2750,
      duration: '60-90 mins',
      highlights: ['Multi-brand specialists', '90-day parts warranty', 'Digital quote'],
      accentColor: Color(0xFF7C3AED),
    ),

    // 🧹 Cleaning
    ServiceItem(
      id: 'cl-deep',
      title: 'Home Deep Cleaning',
      emoji: '🏡',
      category: ServiceCategory.cleaning,
      description: 'Full home, kitchen, bathroom deep clean',
      priceRange: '₹2,000 – ₹8,000',
      startingPrice: 2000,
      rating: 4.90,
      reviewsCount: 5200,
      duration: '3-5 hours',
      highlights: ['Industrial grade machines', 'Eco-safe chemicals', '3-5 member crew'],
      isPopular: true,
      accentColor: Color(0xFF059669),
    ),
    ServiceItem(
      id: 'cl-bath',
      title: 'Bathroom Cleaning',
      emoji: '🛁',
      category: ServiceCategory.cleaning,
      description: 'Tiles, commode, fixtures deep clean',
      priceRange: '₹600 – ₹1,500',
      startingPrice: 600,
      rating: 4.85,
      reviewsCount: 4100,
      duration: '45-60 mins',
      highlights: ['Hard water stain removal', 'Disinfection included', 'Shining fixtures'],
      accentColor: Color(0xFF0D9488),
    ),
    ServiceItem(
      id: 'cl-sofa',
      title: 'Sofa & Carpet Clean',
      emoji: '🪑',
      category: ServiceCategory.cleaning,
      description: 'Dry clean, stain removal, steam clean',
      priceRange: '₹800 – ₹3,000',
      startingPrice: 800,
      rating: 4.83,
      reviewsCount: 1850,
      duration: '60-90 mins',
      highlights: ['Hot water extraction', 'Fabric safe treatment', 'Drying in 2-3 hrs'],
      accentColor: Color(0xFF4F46E5),
    ),
    ServiceItem(
      id: 'cl-pest',
      title: 'Pest Control',
      emoji: '🐛',
      category: ServiceCategory.cleaning,
      description: 'Cockroach, termite, bed bug, rat',
      priceRange: '₹800 – ₹4,000',
      startingPrice: 800,
      rating: 4.78,
      reviewsCount: 2300,
      duration: '45-90 mins',
      highlights: ['Odorless gel & spray', 'Govt approved formulas', 'Warranty re-visit'],
      accentColor: Color(0xFF65A30D),
    ),
    ServiceItem(
      id: 'cl-tank',
      title: 'Water Tank Cleaning',
      emoji: '💧',
      category: ServiceCategory.cleaning,
      description: 'Underground and overhead tank cleaning',
      priceRange: '₹800 – ₹2,500',
      startingPrice: 800,
      rating: 4.81,
      reviewsCount: 940,
      duration: '60-120 mins',
      highlights: ['6-stage cleaning process', 'UV antibacterial treatment', 'Sludge pump out'],
      accentColor: Color(0xFF0284C7),
    ),
    ServiceItem(
      id: 'cl-car',
      title: 'Car Cleaning',
      emoji: '🚗',
      category: ServiceCategory.cleaning,
      description: 'Interior, exterior, foam wash, detailing',
      priceRange: '₹500 – ₹3,000',
      startingPrice: 500,
      rating: 4.84,
      reviewsCount: 2890,
      duration: '45-90 mins',
      highlights: ['High-pressure waterless/foam wash', 'Interior vacuuming', 'Tire shine'],
      accentColor: Color(0xFFDC2626),
    ),

    // 💇 Beauty & Wellness (Women)
    ServiceItem(
      id: 'bw-salon',
      title: 'Salon at Home',
      emoji: '💅',
      category: ServiceCategory.beautyWomen,
      description: 'Waxing, threading, facial, cleanup',
      priceRange: '₹200 – ₹2,000',
      startingPrice: 200,
      rating: 4.92,
      reviewsCount: 7800,
      duration: '45-120 mins',
      highlights: ['Single-use disposable kits', 'Top branded products', 'Clean up after service'],
      isPopular: true,
      accentColor: Color(0xFFDB2777),
    ),
    ServiceItem(
      id: 'bw-massage',
      title: 'Massage Therapy',
      emoji: '💆',
      category: ServiceCategory.beautyWomen,
      description: 'Swedish, deep tissue, aromatherapy',
      priceRange: '₹800 – ₹2,500',
      startingPrice: 800,
      rating: 4.91,
      reviewsCount: 3600,
      duration: '60-90 mins',
      highlights: ['Certified female therapists', 'Premium aromatic oils', 'Massage bed setup'],
      accentColor: Color(0xFF9333EA),
    ),
    ServiceItem(
      id: 'bw-skincare',
      title: 'Skincare & Facial',
      emoji: '🧖',
      category: ServiceCategory.beautyWomen,
      description: 'Anti-aging, hydrating, cleanup facial',
      priceRange: '₹800 – ₹3,000',
      startingPrice: 800,
      rating: 4.87,
      reviewsCount: 2400,
      duration: '60-75 mins',
      highlights: ['Skin-type specific serums', 'Gold & pearl options', 'Facial massage'],
      accentColor: Color(0xFFEC4899),
    ),
    ServiceItem(
      id: 'bw-hair',
      title: 'Haircut & Style',
      emoji: '💈',
      category: ServiceCategory.beautyWomen,
      description: 'Cut, colour, keratin, smoothening',
      priceRange: '₹400 – ₹3,000',
      startingPrice: 400,
      rating: 4.85,
      reviewsCount: 1950,
      duration: '45-90 mins',
      highlights: ['Hair consultation', 'L’Oréal / Matrix products', 'Blow dry included'],
      accentColor: Color(0xFFBE185D),
    ),
    ServiceItem(
      id: 'bw-mehendi',
      title: 'Mehendi',
      emoji: '🌿',
      category: ServiceCategory.beautyWomen,
      description: 'Bridal, party, festival designs',
      priceRange: '₹500 – ₹3,000',
      startingPrice: 500,
      rating: 4.89,
      reviewsCount: 1100,
      duration: '60-180 mins',
      highlights: ['100% natural organic henna', 'Dark stain guarantee', 'Custom bridal styles'],
      accentColor: Color(0xFF15803D),
    ),
    ServiceItem(
      id: 'bw-pedicure',
      title: 'Pedicure & Manicure',
      emoji: '🧴',
      category: ServiceCategory.beautyWomen,
      description: 'Classic, gel, nail art',
      priceRange: '₹400 – ₹1,800',
      startingPrice: 400,
      rating: 4.86,
      reviewsCount: 2800,
      duration: '45-75 mins',
      highlights: ['Sterilized tools', 'Sea salt scrub & massage', 'Premium gel polishes'],
      accentColor: Color(0xFFF43F5E),
    ),

    // 🧔 Men's Grooming
    ServiceItem(
      id: 'mg-haircut',
      title: 'Haircut at Home',
      emoji: '✂️',
      category: ServiceCategory.mensGrooming,
      description: 'Cut, beard trim, styling',
      priceRange: '₹200 – ₹600',
      startingPrice: 200,
      rating: 4.88,
      reviewsCount: 4900,
      duration: '30-45 mins',
      highlights: ['Single-use cape', 'Disinfected trimmers', 'Post-cut head massage'],
      isPopular: true,
      accentColor: Color(0xFF1E293B),
    ),
    ServiceItem(
      id: 'mg-shave',
      title: 'Shave & Beard Grooming',
      emoji: '🪒',
      category: ServiceCategory.mensGrooming,
      description: 'Shave, beard shape, cleanup',
      priceRange: '₹200 – ₹500',
      startingPrice: 200,
      rating: 4.84,
      reviewsCount: 3200,
      duration: '20-35 mins',
      highlights: ['Hot towel treatment', 'Fresh safety blades', 'Aftershave balm'],
      accentColor: Color(0xFF334155),
    ),
    ServiceItem(
      id: 'mg-massage',
      title: "Men's Massage",
      emoji: '💆‍♂️',
      category: ServiceCategory.mensGrooming,
      description: 'Body massage, head massage',
      priceRange: '₹700 – ₹2,000',
      startingPrice: 700,
      rating: 4.87,
      reviewsCount: 2100,
      duration: '45-90 mins',
      highlights: ['Deep tissue stress relief', 'Herbal pain oils', 'Expert male masseurs'],
      accentColor: Color(0xFF475569),
    ),
    ServiceItem(
      id: 'mg-facial',
      title: "Men's Facial",
      emoji: '🧴',
      category: ServiceCategory.mensGrooming,
      description: 'Cleanup, de-tan, acne care',
      priceRange: '₹400 – ₹1,500',
      startingPrice: 400,
      rating: 4.81,
      reviewsCount: 1650,
      duration: '40-60 mins',
      highlights: ['Charcoal de-tan pack', 'Blackhead extraction', 'Hydrating cooling mask'],
      accentColor: Color(0xFF0F766E),
    ),

    // 🏠 On-Demand Home Help
    ServiceItem(
      id: 'hh-cook',
      title: 'Cook at Home',
      emoji: '🍳',
      category: ServiceCategory.homeHelp,
      description: 'Daily cook, party cook, monthly plans',
      priceRange: '₹8,000 – ₹20,000/mo',
      startingPrice: 8000,
      rating: 4.83,
      reviewsCount: 1200,
      duration: 'Custom Schedule',
      highlights: ['Cuisine specialists (North/South/Continental)', 'Hygiene background verified', 'Free replacement guarantee'],
      isPopular: true,
      accentColor: Color(0xFFEA580C),
    ),
    ServiceItem(
      id: 'hh-nanny',
      title: 'Babysitter / Nanny',
      emoji: '👶',
      category: ServiceCategory.homeHelp,
      description: 'Daily, hourly, overnight care',
      priceRange: '₹10,000 – ₹25,000/mo',
      startingPrice: 10000,
      rating: 4.89,
      reviewsCount: 880,
      duration: 'Hourly / Monthly',
      highlights: ['Child safety certified', 'Police verified', 'Infant & toddler trained'],
      accentColor: Color(0xFFF59E0B),
    ),
    ServiceItem(
      id: 'hh-maid',
      title: 'Maid / Home Help',
      emoji: '🧹',
      category: ServiceCategory.homeHelp,
      description: 'Daily cleaning, utensils, mopping',
      priceRange: '₹6,000 – ₹12,000/mo',
      startingPrice: 6000,
      rating: 4.79,
      reviewsCount: 2900,
      duration: 'Daily visits',
      highlights: ['Verified attendance tracking', 'Trained housekeeping standards', 'Substitute available on leaves'],
      accentColor: Color(0xFF10B981),
    ),
    ServiceItem(
      id: 'hh-elder',
      title: 'Elder Care',
      emoji: '👴',
      category: ServiceCategory.homeHelp,
      description: 'Caregiver, nurse, companion',
      priceRange: '₹15,000 – ₹40,000/mo',
      startingPrice: 15000,
      rating: 4.92,
      reviewsCount: 650,
      duration: '12hr / 24hr Live-in',
      highlights: ['Medical & mobility assistance', 'Vitals monitoring', 'Compassionate trained staff'],
      accentColor: Color(0xFF6366F1),
    ),

    // 🏥 Health at Home
    ServiceItem(
      id: 'ha-physio',
      title: 'Physiotherapy',
      emoji: '🦵',
      category: ServiceCategory.healthAtHome,
      description: 'Joint pain, injury, post-surgery',
      priceRange: '₹800 – ₹2,000/session',
      startingPrice: 800,
      rating: 4.91,
      reviewsCount: 1450,
      duration: '45-60 mins',
      highlights: ['BPT/MPT qualified doctors', 'Rehab exercise equipment', 'Custom recovery plan'],
      accentColor: Color(0xFF0284C7),
    ),
    ServiceItem(
      id: 'ha-trainer',
      title: 'Personal Trainer',
      emoji: '🏋️',
      category: ServiceCategory.healthAtHome,
      description: 'Weight loss, muscle, yoga',
      priceRange: '₹5,000 – ₹15,000/mo',
      startingPrice: 5000,
      rating: 4.87,
      reviewsCount: 980,
      duration: '12-24 sessions/mo',
      highlights: ['Certified fitness coach', 'Nutrition & diet chart', 'Progress body tracking'],
      accentColor: Color(0xFFF97316),
    ),
    ServiceItem(
      id: 'ha-doctor',
      title: 'Doctor Visit',
      emoji: '🩺',
      category: ServiceCategory.healthAtHome,
      description: 'General physician at home',
      priceRange: '₹500 – ₹1,500',
      startingPrice: 500,
      rating: 4.88,
      reviewsCount: 1820,
      duration: '30-45 mins',
      highlights: ['MBBS verified physicians', 'Digital prescription generated', 'Basic vitals & ECG at home'],
      accentColor: Color(0xFF14B8A6),
    ),
    ServiceItem(
      id: 'ha-lab',
      title: 'Lab Tests at Home',
      emoji: '💉',
      category: ServiceCategory.healthAtHome,
      description: 'Blood test, urine, ECG at home',
      priceRange: '₹300 – ₹3,000',
      startingPrice: 300,
      rating: 4.90,
      reviewsCount: 4200,
      duration: '15-20 mins sample collection',
      highlights: ['NABL accredited lab partners', 'Digital reports in 12-24 hrs', 'Smart sample cooling kit'],
      isPopular: true,
      accentColor: Color(0xFFEF4444),
    ),

    // 📦 Native Products (Own Brand)
    ServiceItem(
      id: 'np-ro',
      title: 'Water Purifier (RO)',
      emoji: '💧',
      category: ServiceCategory.nativeProducts,
      description: 'Native branded RO, IoT enabled',
      priceRange: '₹8,000 – ₹15,000',
      startingPrice: 8000,
      rating: 4.93,
      reviewsCount: 2100,
      duration: 'Free 2hr installation',
      highlights: ['Needs zero service for 2 years', 'Smart app filter health check', '10-stage RO+UV+Copper alkaline'],
      isPopular: true,
      accentColor: Color(0xFF0284C7),
    ),
    ServiceItem(
      id: 'np-lock',
      title: 'Smart Door Lock',
      emoji: '🔐',
      category: ServiceCategory.nativeProducts,
      description: 'Electronic door locks, smart home',
      priceRange: '₹5,000 – ₹15,000',
      startingPrice: 5000,
      rating: 4.89,
      reviewsCount: 1350,
      duration: 'Free 90 min installation',
      highlights: ['Fingerprint, PIN, RFID & App unlock', 'Emergency physical key', 'Built-in tamper alarm'],
      accentColor: Color(0xFF475569),
    ),
  ];
}
